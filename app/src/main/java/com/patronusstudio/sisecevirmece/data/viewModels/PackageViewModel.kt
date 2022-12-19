package com.patronusstudio.sisecevirmece.data.viewModels

import android.util.Log
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import com.patronusstudio.sisecevirmece.data.repository.network.PackageNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val packageNetworkRepository: PackageNetworkRepository
) : BaseViewModel() {

    private val _categories = MutableStateFlow<List<PackageCategoryModel>>(listOf())
    val categories: StateFlow<List<PackageCategoryModel>> = _categories

    private val _packages = MutableStateFlow<List<PackageModel>>(listOf())
    val packages: StateFlow<List<PackageModel>> get() = _packages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    suspend fun getPackageCategories() {
        _isLoading.value = true
        val categories = networkRepository.getPackageCategories()
        if (categories.isSuccessful.not() || categories.body() == null || categories.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                categories.body()?.message?.toString() ?: "Bir hatayla karşılaşıldı."
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

    suspend fun getPackageFromCategory(clickedItemId: Int) {
        _isLoading.value = true
        val packageResponse = packageNetworkRepository.getPackageCategories(clickedItemId)
        if (packageResponse.isSuccessful.not() || packageResponse.body() == null || packageResponse.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                packageResponse.body()?.message?.toString() ?: "Bir hatayla karşılaşıldı."
        }
        _packages.value = packageResponse.body()?.packages ?: listOf()
        _isLoading.value = false
        Log.d("Sülo", _packages.value.toString())
    }
}