package com.patronusstudio.sisecevirmece.data.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NormalGameScreenViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository
) : ViewModel() {

    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _truthDareSelected = MutableStateFlow(TruthDareEnum.NOT_SELECTED)
    val truthDareSelected: StateFlow<TruthDareEnum> get() = _truthDareSelected

    private val _bottleTouchListener = MutableStateFlow(BottleTouchListener.INIT)
    val bottleTouchListener: StateFlow<BottleTouchListener> get() = _bottleTouchListener

    private val _truthQuestions = MutableStateFlow(listOf<QuestionDbModel>())
    val truthQuestions: StateFlow<List<QuestionDbModel>> get() = _truthQuestions

    private val _dareQuestions = MutableStateFlow(listOf<QuestionDbModel>())
    val dareQuestions: StateFlow<List<QuestionDbModel>> get() = _dareQuestions

    fun setTruthDareSelected(truthDareEnum: TruthDareEnum) {
        _truthDareSelected.value = truthDareEnum
    }

    fun setBottleTouchListener(bottleTouchListener: BottleTouchListener) {
        _bottleTouchListener.value = bottleTouchListener
    }

    suspend fun getTruthDareQuestions() {
        _isLoading.value = true
        val truthDareDefaultPackageEnum =
            if (truthDareSelected.value == TruthDareEnum.TRUTH) TruthDareDefaultPackageEnum.TRUTH
            else TruthDareDefaultPackageEnum.DARE
        val questions = questionLocalRepository.getQuestionsWithPackageId(
            truthDareDefaultPackageEnum.getPackageCategoryId()
        )
        if (questions.isNotEmpty()) {
            val notShowedQuestions = questions.filter {
                !it.isShowed
            }
            if (notShowedQuestions.isEmpty()) {
                questionLocalRepository.updateAllQuestionsShowStatus(
                    truthDareDefaultPackageEnum.getPackageCategoryId(),
                    false
                )
                questions.forEach {
                    it.isShowed = false
                }
                if (truthDareDefaultPackageEnum == TruthDareDefaultPackageEnum.TRUTH) {
                    _truthQuestions.value = questions.shuffled()
                } else {
                    _dareQuestions.value = questions.shuffled()
                }
            } else {
                if (truthDareDefaultPackageEnum == TruthDareDefaultPackageEnum.TRUTH) {
                    _truthQuestions.value = notShowedQuestions.shuffled()
                } else {
                    _dareQuestions.value = notShowedQuestions.shuffled()
                }
            }
        } else {
            packageControlInDb(truthDareDefaultPackageEnum)
        }
        _isLoading.value = false
    }

    private suspend fun packageControlInDb(truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum) {
        val findedPackage =
            packageLocalRepository.getPackageByName(
                truthDareDefaultPackageEnum.getPackageName(application.applicationContext)
            )
        if (findedPackage == null) {
            packageLocalRepository.addPackages(
                getDbModel(truthDareDefaultPackageEnum)
            )
            val questionList =
                questionListToDbModel(truthDareDefaultPackageEnum)
            questionLocalRepository.addQuestions(questionList)
        }
    }

    private fun getDbModel(
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): PackageDbModel {
        return PackageDbModel(
            cloudPackageCategoryId = truthDareDefaultPackageEnum.getPackageCategoryId(),
            packageImage = truthDareDefaultPackageEnum.getPackageName(application.applicationContext)
                .toByteArray(),
            version = truthDareDefaultPackageEnum.getVersion(),
            packageName = truthDareDefaultPackageEnum.getPackageName(application.applicationContext),
            packageComment = truthDareDefaultPackageEnum.getPackageComment(application.applicationContext),
            createdTime = Calendar.getInstance().time.toString(),
            updatedTime = Calendar.getInstance().time.toString(),
        )
    }

    private fun questionListToDbModel(
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): MutableList<QuestionDbModel> {
        val questions = mutableListOf<QuestionDbModel>()
        truthDareDefaultPackageEnum.getQuestions(application.applicationContext).forEach {
            questions.add(
                QuestionDbModel(
                    localPackagePrimaryId = truthDareDefaultPackageEnum.getPackageCategoryId(),
                    question = it,
                    isShowed = false
                )
            )
        }
        return questions
    }

    fun getRandomQuestion(): QuestionDbModel? {
        return if (_truthDareSelected.value == TruthDareEnum.TRUTH && _truthQuestions.value.isEmpty()) {
            null
        } else if (_truthDareSelected.value == TruthDareEnum.DARE && _dareQuestions.value.isEmpty()) {
            null
        } else {
            if (_truthDareSelected.value == TruthDareEnum.TRUTH) _truthQuestions.value.random()
            else _dareQuestions.value.random()
        }
    }

    suspend fun updateQuestionShowStatu(questionPrimaryId: Int) {
        questionLocalRepository.updateQuestionShowStatu(true, questionPrimaryId)
    }

    suspend fun updateAllQuestionShowStatu() {
        val truthDareDefaultPackageEnum =
            if (truthDareSelected.value == TruthDareEnum.DARE) TruthDareDefaultPackageEnum.DARE
            else TruthDareDefaultPackageEnum.TRUTH
        questionLocalRepository.updateAllQuestionsShowStatus(
            truthDareDefaultPackageEnum.getPackageCategoryId(),
            false
        )
    }

    fun removeQuestionOnList(questionDbModel: QuestionDbModel) {
        if (_truthDareSelected.value == TruthDareEnum.DARE) {
            val list = _dareQuestions.value.toMutableList()
            list.remove(questionDbModel)
            _dareQuestions.value = list
        }
        if (_truthDareSelected.value == TruthDareEnum.TRUTH) {
            val list = _truthQuestions.value.toMutableList()
            list.remove(questionDbModel)
            _truthQuestions.value = list
        }
    }
}