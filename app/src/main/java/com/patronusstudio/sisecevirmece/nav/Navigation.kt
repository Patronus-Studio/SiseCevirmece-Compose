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

    var name = "name"
    var surname = "surname"
    val screenWithArgs = "${screenName}/{$name}/{$surname}"

    val arguments = listOf(
        navArgument(name) {
            NavType.StringType
            nullable = true
        },
        navArgument(surname) {
            NavType.StringType
            nullable = true
        }
    )
}

