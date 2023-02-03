package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

data class AvatarResponseModel(
    val data: List<AvatarModel>,
    override val status:HttpStatusEnum,
    override val message: String
):BaseResponse()