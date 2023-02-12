package com.patronusstudio.sisecevirmece2.data.viewModels

import android.app.Application
import com.patronusstudio.sisecevirmece2.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece2.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece2.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece2.data.repository.local.BackgroundLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.BottleLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NormalGameScreenViewModel @Inject constructor(
    private val application: Application,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository,
    private val bottleLocalRepository: BottleLocalRepository,
    private val backgroundLocalRepository: BackgroundLocalRepository
) : BaseViewModel() {

    private val _truthDareSelected = MutableStateFlow(TruthDareEnum.NOT_SELECTED)
    val truthDareSelected: StateFlow<TruthDareEnum> get() = _truthDareSelected

    private val _bottleTouchListener = MutableStateFlow(BottleTouchListener.INIT)
    val bottleTouchListener: StateFlow<BottleTouchListener> get() = _bottleTouchListener

    private val _truthQuestions = MutableStateFlow(listOf<QuestionDbModel>())
    val truthQuestions: StateFlow<List<QuestionDbModel>> get() = _truthQuestions

    private val _dareQuestions = MutableStateFlow(listOf<QuestionDbModel>())
    val dareQuestions: StateFlow<List<QuestionDbModel>> get() = _dareQuestions

    private val _activeBottle = MutableStateFlow<BottleDbModel?>(null)
    val activeBottle: StateFlow<BottleDbModel?> get() = _activeBottle

    private val _backgroundModel = MutableStateFlow<BackgroundDbModel?>(null)
    val backgroundModel: StateFlow<BackgroundDbModel?> get() = _backgroundModel

    private val _truthPackageDbModel = MutableStateFlow<PackageDbModel?>(null)
    val truthPackageDbModel: StateFlow<PackageDbModel?> get() = _truthPackageDbModel.asStateFlow()

    private val _darePackageDbModel = MutableStateFlow<PackageDbModel?>(null)
    val darePackageDbModel: StateFlow<PackageDbModel?> get() = _darePackageDbModel.asStateFlow()

    fun setTruthDareSelected(truthDareEnum: TruthDareEnum) {
        _truthDareSelected.value = truthDareEnum
    }

    fun setBottleTouchListener(bottleTouchListener: BottleTouchListener) {
        _bottleTouchListener.value = bottleTouchListener
    }

    suspend fun getActiveBackground() {
        _isLoading.value = true
        _backgroundModel.value = backgroundLocalRepository.getActiveBackground()
        _isLoading.value = false
    }

    suspend fun getBottleOnDb() {
        _isLoading.value = true
        _activeBottle.value = bottleLocalRepository.getActiveBottle()
        _isLoading.value = false
    }

    suspend fun getTruthDareQuestions() {
        _isLoading.value = true
        val truthDareDefaultPackageEnum =
            if (truthDareSelected.value == TruthDareEnum.TRUTH) TruthDareDefaultPackageEnum.TRUTH
            else TruthDareDefaultPackageEnum.DARE
        val localPackageDbModel =
            packageLocalRepository.getPackageByName(
                truthDareDefaultPackageEnum.getPackageName(
                    application.applicationContext
                )
            )
        if (localPackageDbModel?.packageName == TruthDareEnum.TRUTH.getText(application.applicationContext)) {
            _truthPackageDbModel.value = localPackageDbModel
        } else if (localPackageDbModel?.packageName == TruthDareEnum.DARE.getText(application.applicationContext)) {
            _darePackageDbModel.value = localPackageDbModel
        }

        val questions = questionLocalRepository.getQuestionsWithPackageId(
            localPackageDbModel!!.primaryId
        )
        if (questions.isNotEmpty()) {
            val notShowedQuestions = questions.filter {
                it.isShowed == 0
            }
            if (notShowedQuestions.isEmpty()) {
                questionLocalRepository.updateAllQuestionsShowStatus(
                    localPackageDbModel.primaryId,
                    0
                )
                questions.forEach {
                    it.isShowed = 0
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
            val packagePrimaryId = packageLocalRepository.addPackages(
                getDbModel(truthDareDefaultPackageEnum)
            )
            val questionList =
                questionListToDbModel(truthDareDefaultPackageEnum, packagePrimaryId)
            questionLocalRepository.addQuestions(questionList)
        }
    }

    private fun getDbModel(
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): PackageDbModel {
        return PackageDbModel(
            cloudPackageCategoryId = truthDareDefaultPackageEnum.getCloudPackageCategoryId(),
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
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum,
        packagePrimaryId: Long
    ): MutableList<QuestionDbModel> {
        val questions = mutableListOf<QuestionDbModel>()
        truthDareDefaultPackageEnum.getQuestions(application.applicationContext).forEach {
            questions.add(
                QuestionDbModel(
                    localPackagePrimaryId = packagePrimaryId.toInt(),
                    question = it,
                    isShowed = 0, punishment = null, correctAnswer = null
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
        val packagePrimaryId =
            if (truthDareSelected.value == TruthDareEnum.DARE) _darePackageDbModel.value!!.primaryId
            else _truthPackageDbModel.value!!.primaryId
        questionLocalRepository.updateAllQuestionsShowStatus(
            packagePrimaryId,
            0
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