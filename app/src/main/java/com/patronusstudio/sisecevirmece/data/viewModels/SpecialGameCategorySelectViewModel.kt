package com.patronusstudio.sisecevirmece.data.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SpecialGameCategorySelectViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
) : ViewModel() {

    private val _packages = MutableStateFlow<List<PackageDbModel>>(listOf())
    val packages: StateFlow<List<PackageDbModel>> get() = _packages

    suspend fun getAllPackages() {
        val fetchedPackages = packageLocalRepository.getPackages()
        val truthPackageName =
            TruthDareDefaultPackageEnum.TRUTH.getPackageName(application.applicationContext)
        val darePackageName =
            TruthDareDefaultPackageEnum.DARE.getPackageName(application.applicationContext)
        val filteredPackages = fetchedPackages.filter {
            it.packageName != truthPackageName && it.packageName != darePackageName
        }
        _packages.value = filteredPackages
    }
}