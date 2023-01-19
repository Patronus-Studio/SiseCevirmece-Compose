package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NormalGameScreenViewModel @Inject constructor(
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository
) : ViewModel() {

    private val _truthDareSelected = MutableStateFlow(TruthDareEnum.NOT_SELECTED)
    val truthDareSelected: StateFlow<TruthDareEnum> get() = _truthDareSelected

    private val _bottleTouchListener = MutableStateFlow(BottleTouchListener.INIT)
    val bottleTouchListener: StateFlow<BottleTouchListener> get() = _bottleTouchListener

    private val _truthQuestions = MutableStateFlow(listOf<QuestionDbModel>())
    val truthQuestions: StateFlow<List<QuestionDbModel>> get() = _truthQuestions

    fun setTruthDareSelected(truthDareEnum: TruthDareEnum) {
        _truthDareSelected.value = truthDareEnum
    }

    fun setBottleTouchListener(bottleTouchListener: BottleTouchListener) {
        _bottleTouchListener.value = bottleTouchListener
    }

    suspend fun getTruthDareQuestions(
        context: Context,
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ) {
        //loading status basılacak
        val truthQuestions = questionLocalRepository.getQuestionsWithPackageId(
            context, truthDareDefaultPackageEnum.getPackageCategoryId()
        )
        //SORULARIN GOSTERİLME DURUMUNA GÖRE FİLTRELEME YAPTIM
        if (truthQuestions.isNotEmpty()) {
            val notShowedQuestions = truthQuestions.filter {
                !it.isShowed
            }
            if (notShowedQuestions.isEmpty()) {
                questionLocalRepository.updateAllQuestionsShowStatus(
                    context, truthDareDefaultPackageEnum.getPackageCategoryId(),
                    false
                )
                truthQuestions.forEach {
                    it.isShowed = false
                }
                _truthQuestions.value = truthQuestions.shuffled()
            } else {
                _truthQuestions.value = notShowedQuestions.shuffled()
            }
        }
        //soru hiç yoksa baştan ekleme yap doğruluk cesaret sorularını
        else {

        }
    }
}