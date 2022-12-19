package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName

import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum

data class PackageCategoryModel(
    val activeBtnColor: String,
    val activeTextColor: String,
    val id: Double,
    val isSelected: SelectableEnum,
    val isUserSelectableWhenCreatingPackage: SelectableEnum,
    val name: String,
    val passiveBtnColor: String,
    val passiveTextColor: String
)