package com.patronusstudio.sisecevirmece.data.model

import com.google.gson.annotations.SerializedName
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.PackageDetailCardBtnEnum

data class PackageResponseModel(
    @SerializedName("data")
    val packages: List<PackageModel>,
    val message: Any,
    val status: HttpStatusEnum
)

data class PackageModel(
    override val createdTime: String,
    @SerializedName("description")
    override val packageComment: String,
    val id: Int,
    val imageUrl: String,
    @SerializedName("name")
    override val packageName: String,
    val numberOfDownload: Double,
    val numberOfLike: Double,
    val numberOfUnlike: Double,
    val packageCategory: Double,
    val questions: List<String>,
    override val updatedTime: String,
    val username: String,
    override val version: Int,
    //for local control
    var imageId: Int? = null,
    var packageStatu: PackageDetailCardBtnEnum
) : BasePackageModel()


abstract class BasePackageModel {
    abstract val packageComment: String
    abstract val packageName: String
    abstract val createdTime: String
    abstract val updatedTime: String
    abstract val version: Int
}