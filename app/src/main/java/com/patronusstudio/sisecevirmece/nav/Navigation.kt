package com.patronusstudio.sisecevirmece

interface InAppNavigations {
    val screenName: String
}

object NavLoginScreen : InAppNavigations {
    override val screenName: String
        get() = "LoginScreen"
}

object NavHomeScreen : InAppNavigations {
    override val screenName: String
        get() = "HomeScreen"
}