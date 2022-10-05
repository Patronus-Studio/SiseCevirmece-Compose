package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.ui.screens.GenderEnum

data class UserModelRegister(
    val username: String = "iamcodder2",
    val email:String  = "me.iamcodder2@gmail.com",
    val password: String = "123123",
    val gender: String = GenderEnum.NONE.enumType,
    val token: String = ""
)