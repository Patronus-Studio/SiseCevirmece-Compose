package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class LoginResponseModel(
    val data: Any,
    val status: HttpStatusEnum,
    val message: String? = null
)
