package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.abstarcts.BaseModelWithIndex

data class QuestionModel(
    override var id: Int,
    var question: String
) : BaseModelWithIndex() {
    override fun equals(other: Any?): Boolean = false
}
