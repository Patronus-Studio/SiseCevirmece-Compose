package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class UserInfoModelResponse(
    val data: UserInfoModel,
    override val status: HttpStatusEnum,
    override val message: String?
): BaseResponse()

data class UserInfoModel(
    val achievement: Any,
    val bottleFlipCount: Int,
    val buyedAvatars: String,
    val currentAvatar: String,
    val level: Int,
    val myBottles: Any,
    val myPackages: Any,
    val starCount: Int,
    val username: String
)