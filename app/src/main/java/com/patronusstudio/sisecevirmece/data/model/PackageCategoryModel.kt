package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum

data class PackageCategoryModel(
    override val activeBtnColor: String,
    override val activeTextColor: String,
    val id: Double,
    override val isSelected: SelectableEnum,
    val isUserSelectableWhenCreatingPackage: SelectableEnum,
    val name: String,
    override val passiveBtnColor: String,
    override val passiveTextColor: String
) : BaseCategoryModel()