package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryResponseModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository
): BaseViewModel() {

    private val _packageCategories = MutableStateFlow<PackageCategoryResponseModel?>(null)
    val packageCategories: StateFlow<PackageCategoryResponseModel?> = _packageCategories

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    suspend fun getPackageCategories() {
        _isLoading.value = true
        val categories = networkRepository.getPackageCategories()
        if (categories.isSuccessful.not() || categories.body() == null || categories.body()?.status != HttpStatusEnum.OK.code) {
            _errorMessage.value = categories.body()?.message?.toString() ?: "Bir hatayla karşılaşıldı."
        }
        _isLoading.value = false
    }

}