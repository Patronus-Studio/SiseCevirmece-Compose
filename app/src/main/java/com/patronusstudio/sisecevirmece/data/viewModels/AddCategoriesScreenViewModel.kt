package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.ui.screens.BaseModelWithIndex
import com.patronusstudio.sisecevirmece.ui.screens.QuestionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddCategoriesScreenViewModel : ViewModel() {

    private val _questionList = MutableStateFlow(
        mutableStateListOf(
            QuestionModel(0, ""),
            QuestionModel(1, ""),
            QuestionModel(2, "")
        )
    )
    val questionList: StateFlow<List<QuestionModel>> get() = _questionList

    fun updateQuestionModelText(questionModel: QuestionModel, newText: String) {
        questionModel.question = newText
        _questionList.value[questionModel.id] = questionModel
    }

    fun removeQuestionModel(questionModel: QuestionModel) {
        val result = questionModel.removeModelOnList(_questionList.value)
        _questionList.value.clear()
        _questionList.value.addAll(result)
    }

    fun addNewQuestionModel() {
        _questionList.value.add(QuestionModel(_questionList.value.last().id + 1, ""))
    }
}

fun <T : BaseModelWithIndex> T.removeModelOnList(list: List<T>): List<T> {
    val newList: MutableList<T> = list.toMutableList()
    return newList.filter {
        it.id != this.id
    }.map {
        if (it.id > this.id) {
            it.id = it.id - 1
            it
        } else it
    }
}