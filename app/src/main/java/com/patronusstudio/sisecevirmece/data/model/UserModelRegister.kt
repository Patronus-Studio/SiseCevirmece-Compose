package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.GenderEnum

data class UserModelRegister(
    val username: String = "",
    val email:String  = "",
    val password: String = "",
    val gender: String = GenderEnum.NONE.enumType,
    val token: String = ""
)