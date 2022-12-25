package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class PackageResponseModel(
    @SerializedName("data")
    val packages: List<PackageModel>,
    val message: Any,
    val status: HttpStatusEnum
)

data class PackageModel(
    val createdTime: String,
    val description: String,
    val id: Double,
    val imageUrl: String,
    val name: String,
    val numberOfDownload: Double,
    val numberOfLike: Double,
    val numberOfUnlike: Double,
    val packageCategory: Double,
    val questions: List<Double>,
    val updatedTime: String,
    val username: String,
    val version: Double,
    var imageId: Int? = null
)