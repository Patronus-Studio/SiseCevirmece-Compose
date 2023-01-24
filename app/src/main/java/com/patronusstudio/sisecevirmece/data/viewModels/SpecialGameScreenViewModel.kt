package com.patronusstudio.sisecevirmece.data.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
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
    private val questionLocalRepository: QuestionLocalRepository
) : ViewModel() {

    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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


    private suspend fun getQuestions(packageDbModel: PackageDbModel) {
        val packageId = when (packageDbModel.cloudPackageCategoryId) {
            TruthDareDefaultPackageEnum.TRUTH.getPackageCategoryId() -> {
                TruthDareDefaultPackageEnum.TRUTH.getPackageCategoryId()
            }
            TruthDareDefaultPackageEnum.DARE.getPackageCategoryId() -> {
                TruthDareDefaultPackageEnum.DARE.getPackageCategoryId()
            }
            else -> packageDbModel.primaryId
        }
        val questions = questionLocalRepository.getQuestionsWithPackageId(packageId)
        val filteredQuestions = questions.filter { it.isShowed.not() }
        if (filteredQuestions.isEmpty()) {
            withContext(Dispatchers.IO) {
                questionLocalRepository.updateAllQuestionsShowStatus(
                    packageDbModel.primaryId, false
                )
            }
            questions.forEach { it.isShowed = false }
            Log.d("Sülo", "getQuestions: ${packageDbModel.packageName} ${questions.size}")
        } else {
            _packagesAndQuestions.value[packageDbModel.primaryId] = filteredQuestions
            Log.d("Sülo", "getQuestions: ${packageDbModel.packageName} ${filteredQuestions.size}")
        }
    }

    fun getRandomPackage() {
        _randomPackage.value = _selectedLists.value!!.random()
    }

    suspend fun getRandomQuestion() {
        _isLoading.value = true
        val randomQst = _packagesAndQuestions.value[_randomPackage.value!!.primaryId]?.random()
        if (randomQst == null) {
            questionLocalRepository.updateQuestionShowStatu(false, _randomPackage.value!!.primaryId)
            val fetchedQuestions =
                questionLocalRepository.getQuestionsWithPackageId(_randomPackage.value!!.primaryId)
            _packagesAndQuestions.value[_randomPackage.value!!.primaryId] = fetchedQuestions
            getRandomQuestion()
        } else {
            _randomQuestion.value = randomQst
            questionLocalRepository.updateQuestionShowStatu(true, randomQuestion.value!!.primaryId)
            val listOnMap = packagesAndQuestions.value[_randomPackage.value!!.primaryId]
            listOnMap!!.toMutableList().remove(randomQuestion.value)
            _packagesAndQuestions.value[_randomPackage.value!!.primaryId] = listOnMap
        }
        _isLoading.value = false
    }

}