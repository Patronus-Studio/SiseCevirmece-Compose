package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

data class UserModelResponse(
    val data: String?,
    override val message: String?,
    override val status: HttpStatusEnum
) : BaseResponse()