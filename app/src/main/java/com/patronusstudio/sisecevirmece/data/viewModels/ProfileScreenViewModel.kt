package com.patronusstudio.sisecevirmece.data.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.ProfileTitlesEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.ProfileCategoryModel
import com.patronusstudio.sisecevirmece.data.repository.local.BottleLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
    private val bottleLocalRepository: BottleLocalRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _titles = MutableStateFlow<List<BaseCategoryModel>>(listOf())
    val titles: StateFlow<List<BaseCategoryModel>> get() = _titles

    private val _currentTitle = MutableStateFlow<ProfileCategoryModel?>(null)
    val currentTitle: StateFlow<ProfileCategoryModel?> get() = _currentTitle

    private val _packages = MutableStateFlow<List<PackageDbModel>>(listOf())
    val packages: StateFlow<List<PackageDbModel>> get() = _packages

    private val _bottles = MutableStateFlow<List<BottleDbModel>>(listOf())
    val bottles: StateFlow<List<BottleDbModel>> get() = _bottles

    init {
        val list = List(3) {
            ProfileCategoryModel(
                id = it,
                activeBtnColor = ProfileTitlesEnum.getEnumWithIndex(it).getActiveBtnColor(),
                activeTextColor = ProfileTitlesEnum.getEnumWithIndex(it).getActiveTextColor(),
                passiveBtnColor = ProfileTitlesEnum.getEnumWithIndex(it).getPassiveBtnColor(),
                passiveTextColor = ProfileTitlesEnum.getEnumWithIndex(it).getPassiveTextColor(),
                isSelected = if (it == 0) SelectableEnum.YES else SelectableEnum.NO,
                name = ProfileTitlesEnum.getEnumWithIndex(it)
                    .getTitles(application.applicationContext)
            )
        }
        _titles.value = list
        _currentTitle.value = _titles.value.first() as ProfileCategoryModel
    }

    fun clickedBtn(clickedItemId: Int) {
        _isLoading.value = true
        val findedActiveBtnIndex = _titles.value.indexOfFirst {
            it.isSelected == SelectableEnum.YES
        }
        if (findedActiveBtnIndex == -1 || findedActiveBtnIndex == clickedItemId) return
        val tempList = mutableListOf<ProfileCategoryModel>()
        _titles.value.forEach {
            it as ProfileCategoryModel
            if (findedActiveBtnIndex == it.id) tempList.add(it.copy(isSelected = SelectableEnum.NO))
            else if (clickedItemId == it.id) tempList.add(it.copy(isSelected = SelectableEnum.YES))
            else tempList.add(it.copy())
        }
        _titles.value = tempList
        _isLoading.value = false
    }

    fun setTitle(profileCategoryModel: ProfileCategoryModel) {
        _currentTitle.value = profileCategoryModel
    }

    suspend fun getDatas(profileCategoryModel: ProfileCategoryModel){
        _bottles.value = listOf()
        _packages.value = listOf()
        when(profileCategoryModel.id){
            0 -> getPackages()
            1-> getBottles()
            // TODO: getbackgrounds eklenecek
        }
    }

    suspend fun getPackages() {
        _isLoading.value = true
        _packages.value = packageLocalRepository.getPackages()
        _isLoading.value = false
    }

    suspend fun getBottles() {
        _isLoading.value = true
        _bottles.value = bottleLocalRepository.getBottles()
        _isLoading.value = false
    }

}