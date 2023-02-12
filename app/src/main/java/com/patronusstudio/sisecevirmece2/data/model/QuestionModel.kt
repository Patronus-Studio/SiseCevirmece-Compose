package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.abstarcts.BaseModelWithIndex

data class QuestionModel(
    override var id: Int,
    var question: String,
    var correctAnswer: String,
    var punishment: String?
) : BaseModelWithIndex() {
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + question.hashCode()
        result = 31 * result + correctAnswer.hashCode()
        result = 31 * result + (punishment?.hashCode() ?: 0)
        return result
    }
}
