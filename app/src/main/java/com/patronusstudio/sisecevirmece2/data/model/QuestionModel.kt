package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.abstarcts.BaseModelWithIndex

data class QuestionModel(
    override var id: Int,
    var question: String
) : BaseModelWithIndex() {
    override fun equals(other: Any?): Boolean = false
}