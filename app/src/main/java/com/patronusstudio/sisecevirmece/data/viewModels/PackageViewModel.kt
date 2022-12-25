package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import android.util.Log
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.network.PackageNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _packages = MutableStateFlow<List<PackageModel>>(listOf())
    val packages: StateFlow<List<PackageModel>> get() = _packages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


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
            }
        }
        _packages.value = networkPackages.body()?.packages ?: listOf()
        _isLoading.value = false
        Log.d("Sülo", _packages.value.toString())
    }
}