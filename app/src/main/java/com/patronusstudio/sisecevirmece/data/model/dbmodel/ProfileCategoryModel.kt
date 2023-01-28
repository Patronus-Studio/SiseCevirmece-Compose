package com.patronusstudio.sisecevirmece.data.model.dbmodel

import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BaseCategoryModel

data class ProfileCategoryModel(
    val id: Int,
    override val activeBtnColor: String,
    override val activeTextColor: String,
    override val passiveBtnColor: String,
    override val passiveTextColor: String,
    override val isSelected: SelectableEnum,
    val name: String
) : BaseCategoryModel()