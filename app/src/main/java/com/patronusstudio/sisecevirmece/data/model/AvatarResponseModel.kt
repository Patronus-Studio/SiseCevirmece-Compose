package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class AvatarResponseModel(
    val data: List<AvatarModel>,
    override val status:HttpStatusEnum,
    override val message: String
):BaseResponse()