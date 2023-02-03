package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

data class LoginResponseModel(
    val data: Any,
    val status: HttpStatusEnum,
    val message: String? = null
)
