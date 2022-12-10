package com.patronusstudio.sisecevirmece.data.model

data class PackageCategoryResponseModel(
    val packageCategoryModel: List<PackageCategoryModel>,
    val message: Any,
    val status: String
)