package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName

data class PackageCategoryModel(
    val activeBtnColor: String,
    val activeTextColor: String,
    val id: Int,
    var isSelected: YESORNO,
    val name: String,
    val passiveBtnColor: String,
    val passiveTextColor: String
)

enum class YESORNO(val value:String){
    @SerializedName("Y")
    YES("Y"),
    @SerializedName("N")
    NO("N")
}