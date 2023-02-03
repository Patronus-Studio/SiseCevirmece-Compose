package com.patronusstudio.sisecevirmece2.data.viewModels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.data.repository.local.PackageLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpecialGameCategorySelectViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
) : ViewModel() {

    private val _packages = mutableStateListOf<PackageDbModel>()
    val packages: SnapshotStateList<PackageDbModel> get() = _packages

    private val _selectedPackages = mutableStateListOf<PackageDbModel>()
    val selectedPackage: SnapshotStateList<PackageDbModel> get() = _selectedPackages

    init {
        _packages.clear()
        _selectedPackages.clear()
    }

    fun clearData() {
        _selectedPackages.clear()
        _packages.clear()
    }

    suspend fun getAllPackages() {
        val fetchedPackages = packageLocalRepository.getPackages()
        _packages.clear()
        _packages.addAll(fetchedPackages)
    }

    fun setShowStatu(packageDbModel: PackageDbModel) {
        val mutableList = mutableListOf<PackageDbModel>()
        _packages.forEach {
            if (packageDbModel.primaryId == it.primaryId) {
                mutableList.add(it.copy(isSelected = packageDbModel.isSelected.not()))
            } else mutableList.add(it.copy())
        }
        _packages.clear()
        _packages.addAll(mutableList)
    }

    fun setSelectedPackages() {
        _selectedPackages.clear()
        val list = _packages.filter {
            it.isSelected
        }
        _selectedPackages.addAll(list)
    }

    fun getSelectedPackageJSON(): String = Gson().toJson(_selectedPackages.toList())
}