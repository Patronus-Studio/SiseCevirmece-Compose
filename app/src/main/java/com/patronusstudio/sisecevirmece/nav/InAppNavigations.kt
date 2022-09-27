package com.patronusstudio.sisecevirmece

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface InAppNavigations {
    val screenName:String
}


object NavLoginScreen : InAppNavigations {
    override val screenName: String
        get() = "LoginScreen"
}

object NavHomeScreen : InAppNavigations {
    override val screenName: String
        get() = "HomeScreen"

    var token = "token"
    val screenWithArgs = "${screenName}/{$token}"

    val arguments = listOf(
        navArgument(token) {
            NavType.StringType
            nullable = true
        }
    )
}