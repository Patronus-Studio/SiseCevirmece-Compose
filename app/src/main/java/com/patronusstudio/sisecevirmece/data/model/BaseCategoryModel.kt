package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum

abstract class BaseCategoryModel {
    abstract val activeBtnColor: String
    abstract val activeTextColor: String
    abstract val passiveBtnColor: String
    abstract val passiveTextColor: String
    abstract val isSelected: SelectableEnum

}