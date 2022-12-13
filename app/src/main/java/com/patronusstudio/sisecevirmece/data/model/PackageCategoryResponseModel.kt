package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class PackageCategoryResponseModel(
    @SerializedName("data")
    val packageCategoryModel: List<PackageCategoryModel>,
    val message: String,
    val status: HttpStatusEnum
)