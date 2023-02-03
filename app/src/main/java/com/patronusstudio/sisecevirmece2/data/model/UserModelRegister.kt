package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.GenderEnum

data class UserModelRegister(
    val username: String = "",
    val email:String  = "",
    val password: String = "",
    val gender: String = GenderEnum.NONE.enumType,
    val token: String = ""
)