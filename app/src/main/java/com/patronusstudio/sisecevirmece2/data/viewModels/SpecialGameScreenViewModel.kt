package com.patronusstudio.sisecevirmece2.data.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.patronusstudio.sisecevirmece2.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece2.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece2.data.repository.local.BackgroundLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.BottleLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SpecialGameScreenViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository,
    private val bottleLocalRepository: BottleLocalRepository,
    private val backgroundLocalRepository: BackgroundLocalRepository
) : BaseViewModel() {

    private val _bottleTouchListener = MutableStateFlow(BottleTouchListener.INIT)
    val bottleTouchListener: StateFlow<BottleTouchListener> get() = _bottleTouchListener

    private val _selectedLists = MutableStateFlow<List<PackageDbModel>?>(null)
    val selectedLists: StateFlow<List<PackageDbModel>?> get() = _selectedLists

    private val _packagesAndQuestions =
        MutableStateFlow<HashMap<Int, List<QuestionDbModel>>>(hashMapOf())
    val packagesAndQuestions: StateFlow<HashMap<Int, List<QuestionDbModel>>> get() = _packagesAndQuestions

    private val _randomPackage = MutableStateFlow<PackageDbModel?>(null)
    val randomPackage: StateFlow<PackageDbModel?> get() = _randomPackage

    private val _randomQuestion = MutableStateFlow<QuestionDbModel?>(null)
    val randomQuestion: StateFlow<QuestionDbModel?> get() = _randomQuestion

    private val _activeBottle = MutableStateFlow<BottleDbModel?>(null)
    val activeBottle : StateFlow<BottleDbModel?> get() = _activeBottle


    private val _backgroundModel = MutableStateFlow<BackgroundDbModel?>(null)
    val backgroundModel: StateFlow<BackgroundDbModel?> get() = _backgroundModel


    suspend fun getActiveBackground(){
        _backgroundModel.value = backgroundLocalRepository.getActiveBackground()
    }

    suspend fun jsonToModel(jsonModel: String) {
        _packagesAndQuestions.value = hashMapOf()

        val listType = object : TypeToken<ArrayList<PackageDbModel?>?>() {}.type
        val packages: List<PackageDbModel> = Gson().fromJson(jsonModel, listType)
        _selectedLists.value = packages
        coroutineScope {
            async(Dispatchers.IO) {
                packages.forEach {
                    getQuestions(it)
                }
            }.await()
            async {
                getRandomPackage()
            }.await()
            async {
                getRandomQuestion()
            }.await()
        }
    }

    fun setBottleTouchListener(bottleTouchListener: BottleTouchListener) {
        _bottleTouchListener.value = bottleTouchListener
    }

    suspend fun getBottleOnDb(){
        _isLoading.value = true
        _activeBottle.value = bottleLocalRepository.getActiveBottle()
        _isLoading.value = false
    }

    private suspend fun getQuestions(packageDbModel: PackageDbModel) {
        val questions =
            questionLocalRepository.getQuestionsWithPackageId(packageDbModel.primaryId)
        val filteredQuestions = questions.filter { it.isShowed == 0 }
        if (filteredQuestions.isEmpty()) {
            withContext(Dispatchers.IO) {
                questionLocalRepository.updateAllQuestionsShowStatus(
                    packageDbModel.primaryId, 0
                )
            }
            questions.forEach { it.isShowed = 0 }
        } else {
            _packagesAndQuestions.value[packageDbModel.primaryId] = filteredQuestions
        }
    }

    fun getRandomPackage() {
        _randomPackage.value = _selectedLists.value!!.random()
    }

    suspend fun getRandomQuestion() {
        _isLoading.value = true
        val packageId = _randomPackage.value!!.primaryId
        val randomQst = _packagesAndQuestions.value[packageId]?.random()
        if (randomQst == null) {
            withContext(Dispatchers.IO) {
                questionLocalRepository.updateAllQuestionsShowStatus(packageId, 0)
            }
            val fetchedQuestions = withContext(Dispatchers.IO) {
                questionLocalRepository.getQuestionsWithPackageId(packageId)
            }
            _packagesAndQuestions.value[packageId] = fetchedQuestions
            getRandomQuestion()
        } else {
            _randomQuestion.value = randomQst
            questionLocalRepository.updateQuestionShowStatu(true, randomQst.primaryId)
            val listOnMap = packagesAndQuestions.value[packageId]
            listOnMap!!.toMutableList().remove(randomQuestion.value)
            _packagesAndQuestions.value[packageId] = listOnMap
        }
        _isLoading.value = false
    }
}