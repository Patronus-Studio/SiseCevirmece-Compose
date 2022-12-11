package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName

data class PackageCategoryResponseModel(
    @SerializedName("data")
    val packageCategoryModel: List<PackageCategoryModel>,
    val message: Any,
    val status: String
)