package com.patronusstudio.sisecevirmece.data.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.utils.removeModelOnList
import com.patronusstudio.sisecevirmece.ui.screens.QuestionModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddCategoriesScreenViewModel : ViewModel() {

    private val _questionList = MutableStateFlow(
        mutableStateListOf(
            QuestionModel(1, "asdasd"),
            QuestionModel(2, "ads"),
            QuestionModel(3, "da"),
            QuestionModel(4, "asdasd"),
            QuestionModel(5, "ad"))
    )
    val questionList: StateFlow<List<QuestionModel>> get() = _questionList

    private val _packageName = MutableStateFlow("")
    val packageName: StateFlow<String> get() = _packageName

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> get() = _selectedImage

    private val _errorMessage = MutableStateFlow("")
    val errorMessage : StateFlow<String> get() = _errorMessage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading :StateFlow<Boolean> get() = _isLoading

    fun updateQuestionModelText(questionModel: QuestionModel, newText: String) {
        questionModel.question = newText
        _questionList.value[questionModel.id - 1] = questionModel
    }

    fun removeQuestionModel(questionModel: QuestionModel) {
        val result = questionModel.removeModelOnList(_questionList.value)
        _questionList.value.clear()
        _questionList.value.addAll(result)
    }

    fun addNewQuestionModel() {
        _questionList.value.add(QuestionModel(_questionList.value.last().id + 1, ""))
    }

    fun setPackageName(name: String) {
        _packageName.value = name
    }

    fun setBitmap(bitmap: Bitmap) {
        _selectedImage.value = bitmap
    }

    fun saveQuestions() {
        val listIsEmpty = listEmptyControl()
        if(listIsEmpty) {
            _errorMessage.value = "Kendi kategorinizi eklemek için 10 taneden fazla soru ekleyin."
            return
        }
        if(_packageName.value.isEmpty()){
            _errorMessage.value = "Lütfen kategori ismi girin."
            return
        }
        _isLoading.value = true
        // TODO: db kaydetme işlemi yapılacak
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            withContext(Dispatchers.Main){
                _isLoading.value = false
            }
        }
    }

    private fun listEmptyControl(): Boolean {
        val notEmptyQuestionSize = _questionList.value.filter {
            it.question.isNotEmpty() && it.question.isNotBlank()
        }.size
        return notEmptyQuestionSize < 10
    }

    fun clearErrorMessage(){
        _errorMessage.value = ""
    }
}