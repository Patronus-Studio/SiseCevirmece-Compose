package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.network.PackageNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val packageNetworkRepository: PackageNetworkRepository,
    private val packageLocalRepository: PackageLocalRepository
) : BaseViewModel() {

    private val _categories = MutableStateFlow<List<PackageCategoryModel>>(listOf())
    val categories: StateFlow<List<PackageCategoryModel>> = _categories

    var packages = mutableStateListOf<PackageModel>()

    private val _currentPackage = MutableStateFlow<PackageModel?>(null)
    val currentPackage: StateFlow<PackageModel?> get() = _currentPackage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setPackageModel(packageModel: PackageModel) {
        _currentPackage.value = packageModel
    }

    suspend fun getPackageCategories() {
        _isLoading.value = true
        val categories = packageNetworkRepository.getPackageCategories()
        if (categories.isSuccessful.not() || categories.body() == null || categories.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                categories.body()?.message ?: "Bir hatayla karşılaşıldı."
        }
        _categories.value = (categories.body()?.packageCategoryModel
            ?: arrayListOf()) as MutableList<PackageCategoryModel>
        _isLoading.value = false
    }

    fun clickedBtn(clickedItemId: Int) {
        val findedActiveBtnIndex = _categories.value.indexOfFirst {
            it.isSelected == SelectableEnum.YES
        }
        if (findedActiveBtnIndex == -1 || findedActiveBtnIndex == clickedItemId) return

        val tempList = mutableListOf<PackageCategoryModel>()
        _categories.value.forEach {
            if (findedActiveBtnIndex.toDouble() == it.id - 1) tempList.add(it.copy(isSelected = SelectableEnum.NO))
            else if (clickedItemId.toDouble() == it.id - 1) tempList.add(it.copy(isSelected = SelectableEnum.YES))
            else tempList.add(it.copy())
        }
        _categories.value = tempList
    }

    suspend fun getPackageFromCategory(context: Context, clickedItemId: Int) {
        _isLoading.value = true
        val networkPackages = packageNetworkRepository.getPackageCategories(clickedItemId)
        if (networkPackages.isSuccessful.not() || networkPackages.body() == null || networkPackages.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                networkPackages.body()?.message?.toString() ?: "Bir hatayla karşılaşıldı."
        }
        val localPackages = packageLocalRepository.getPackages(context)
        networkPackages.body()?.packages?.forEach { networkModel ->
            val findedModel = localPackages.find { localModel ->
                networkModel.id.toInt() == localModel.cloudPackageCategoryId
            }
            if (findedModel != null) {
                if (findedModel.version < networkModel.version) networkModel.imageId =
                    R.drawable.update
                else networkModel.imageId = R.drawable.tick
                networkModel.packagaStatu = when {
                    findedModel.version == networkModel.version.toInt() -> PackageDetailCardBtnEnum.REMOVE
                    findedModel.version < networkModel.version.toInt() -> PackageDetailCardBtnEnum.UPDATE
                    else -> PackageDetailCardBtnEnum.DOWNLOAD
                }
            } else {
                networkModel.packagaStatu = PackageDetailCardBtnEnum.DOWNLOAD
            }
        }
        packages.clear()
        packages.addAll(networkPackages.body()?.packages ?: listOf())
        _isLoading.value = false
    }

    suspend fun removePackage() {
        _isLoading.value = true
        delay(2000L)
        setPackageStatu(PackageDetailCardBtnEnum.DOWNLOAD)
        updateModelOnList(PackageDetailCardBtnEnum.DOWNLOAD)
        _isLoading.value = false
    }

    suspend fun downloadPackage() {
        _isLoading.value = true
        delay(2000L)
        setPackageStatu(PackageDetailCardBtnEnum.REMOVE)
        updateModelOnList(PackageDetailCardBtnEnum.REMOVE)
        _isLoading.value = false
    }

    suspend fun updatePackage() {
        _isLoading.value = true
        delay(2000L)
        setPackageStatu(PackageDetailCardBtnEnum.REMOVE)
        updateModelOnList(PackageDetailCardBtnEnum.REMOVE)
        _isLoading.value = false
    }

    private fun setPackageStatu(packageStatu: PackageDetailCardBtnEnum) {
        val newPackage = _currentPackage.value!!.copy(packagaStatu = packageStatu)
        _currentPackage.value = newPackage
    }

    private fun updateModelOnList(packageStatu: PackageDetailCardBtnEnum) {
        val findedIndex = packages.indexOfFirst {
            it.id == currentPackage.value?.id
        }
        if (findedIndex != -1) {
            packages[findedIndex] = packages[findedIndex].copy(packagaStatu = packageStatu)
        }
    }
}