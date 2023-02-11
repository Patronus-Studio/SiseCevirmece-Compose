package com.patronusstudio.sisecevirmece2.data.viewModels

import android.app.Application
import com.patronusstudio.sisecevirmece2.data.enums.ProfileTitlesEnum
import com.patronusstudio.sisecevirmece2.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece2.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.*
import com.patronusstudio.sisecevirmece2.data.repository.local.BackgroundLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.BottleLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
    private val bottleLocalRepository: BottleLocalRepository,
    private val backgroundLocalRepository: BackgroundLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository
) : BaseViewModel() {

    private val _titles = MutableStateFlow<List<BaseCategoryModel>>(listOf())
    val titles: StateFlow<List<BaseCategoryModel>> get() = _titles

    private val _currentTitle = MutableStateFlow<ProfileCategoryModel?>(null)
    val currentTitle: StateFlow<ProfileCategoryModel?> get() = _currentTitle

    private val _packages = MutableStateFlow<MutableList<PackageDbModel>>(mutableListOf())
    val packages: StateFlow<MutableList<PackageDbModel>> get() = _packages

    private val _bottles = MutableStateFlow<List<BottleDbModel>>(listOf())
    val bottles: StateFlow<List<BottleDbModel>> get() = _bottles

    private val _backgrounds = MutableStateFlow<List<BackgroundDbModel>>(listOf())
    val backgrounds: StateFlow<List<BackgroundDbModel>> get() = _backgrounds

    private val _selectedPackage = MutableStateFlow<PackageDbModel?>(null)
    val selectedPackage: StateFlow<PackageDbModel?> = _selectedPackage.asStateFlow()

    private val _questions = MutableStateFlow<List<QuestionDbModel>>(listOf())
    val questions: StateFlow<List<QuestionDbModel>> get() = _questions

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
        if (findedActiveBtnIndex == -1 || findedActiveBtnIndex == clickedItemId) {
            _isLoading.value = false
            return
        }
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

    suspend fun getDatas(profileCategoryModel: ProfileCategoryModel) {
        _bottles.value = listOf()
        _packages.value = mutableListOf()
        delay(100)
        when (profileCategoryModel.id) {
            0 -> getPackages()
            1 -> getBottles()
            2 -> getBackgrounds()
        }
    }

    suspend fun getPackages() {
        _isLoading.value = true
        _packages.value = packageLocalRepository.getPackages().toMutableList()
        _isLoading.value = false
    }

    suspend fun getBottles() {
        _isLoading.value = true
        _bottles.value = bottleLocalRepository.getBottles()
        _isLoading.value = false
    }

    suspend fun getBackgrounds() {
        _isLoading.value = true
        _backgrounds.value = backgroundLocalRepository.getBackgrounds()
        _isLoading.value = false
    }

    fun setBottleActiveStatuOnLocal(clickedItemId: Int) {
        _isLoading.value = true
        val findedActiveBtnIndex = _bottles.value.indexOfFirst {
            it.isActive
        }
        if (findedActiveBtnIndex == -1 || findedActiveBtnIndex == clickedItemId) {
            _isLoading.value = false
            return
        }
        val findedActiveModel = _bottles.value[findedActiveBtnIndex]
        val tempList = mutableListOf<BottleDbModel>()
        _bottles.value.forEach {
            if (findedActiveModel.primaryId == it.primaryId) tempList.add(it.copy(isActive = false))
            else if (clickedItemId == it.primaryId) tempList.add(it.copy(isActive = true))
            else tempList.add(it.copy())
        }
        _bottles.value = tempList
        _isLoading.value = false
    }

    fun setBottleActiveStatuOnDb(clickedItemId: Int) {
        val findedClickedModel = _bottles.value.find {
            clickedItemId == it.primaryId
        }
        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                bottleLocalRepository.updateAllActiveStatu(false)
            }
            delay(200)
            withContext(Dispatchers.IO) {
                bottleLocalRepository.updateActiveStatu(findedClickedModel!!.primaryId, true)
            }
            delay(200)
            _isLoading.value = false
        }
    }

    fun setBackgroundActiveStatuOnLocal(model: BackgroundDbModel) {
        if (model.isActive) return
        _isLoading.value = true
        val tempList = mutableListOf<BackgroundDbModel>()
        try {
            val findedClickedModel = _backgrounds.value.first {
                it.isActive
            }
            _backgrounds.value.forEach {
                if (model.primaryId == it.primaryId) tempList.add(it.copy(isActive = true))
                else if (findedClickedModel.primaryId == it.primaryId) tempList.add(it.copy(isActive = false))
                else tempList.add(it.copy(isActive = false))
            }
        } catch (e: Exception) {
            _backgrounds.value.forEach {
                if (model.primaryId == it.primaryId) tempList.add(it.copy(isActive = true))
                else tempList.add(it.copy(isActive = false))
            }
        }
        _backgrounds.value = listOf()
        _backgrounds.value = tempList
        _isLoading.value = false
    }

    fun setBackgroundActiveStatuOnDb(model: BackgroundDbModel) {
        if (model.isActive) return
        try {
            CoroutineScope(Dispatchers.Main).launch {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    backgroundLocalRepository.updateAllActiveStatu(false)
                }
                delay(200)
                withContext(Dispatchers.IO) {
                    backgroundLocalRepository.updateActiveStatu(model.primaryId, true)
                }
                delay(200)
                _isLoading.value = false
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    backgroundLocalRepository.updateActiveStatu(model.primaryId, true)
                }
                delay(200)
                _isLoading.value = false
            }
        }
    }

    fun setSelectedPackageModel(packageDbModel: PackageDbModel?) {
        _selectedPackage.value = packageDbModel
    }

    suspend fun removePackage() {
        _isLoading.value = true
        _selectedPackage.value?.let { eachPackage ->
            packageLocalRepository.removePackage(eachPackage.primaryId)
            val tempList = mutableListOf<PackageDbModel>()
            _packages.value.forEach { model ->
                if (model.primaryId != eachPackage.primaryId) tempList.add(model.copy())
            }
            _packages.value = tempList
        }
        _isLoading.value = false
    }

    suspend fun getQuestions() {
        _isLoading.value = true
        _selectedPackage.value?.let {
            val fetchedQuestions = questionLocalRepository.getQuestionsWithPackageId(it.primaryId)
            _questions.value = fetchedQuestions
        }
        _isLoading.value = false
    }

    suspend fun resetQuestions() {
        _isLoading.value = true
        _questions.value.firstOrNull()?.let {
            questionLocalRepository.updateAllQuestionsShowStatus(it.localPackagePrimaryId, 0)
        }
        val tempList = _questions.value.subList(0, _questions.value.size).onEach { it.isShowed = 0 }
        _questions.value = tempList
        delay(300)
        _isLoading.value = false
    }
}