package com.patronusstudio.sisecevirmece

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class  BaseRoutes {
    abstract val routeName: String
}

sealed class NavSplashScreen(override val routeName: String) : BaseRoutes() {
    object SplashScreens : NavSplashScreen("NavSplashScreen")
}

sealed class NavAppLandingScreens(override val routeName: String) : BaseRoutes() {
    object RootNesned: NavAppLandingScreens("NavAppLandingScreens")
    object First : NavAppLandingScreens("First")
    object Second : NavAppLandingScreens("Second")
    object Third : NavAppLandingScreens("Third")
    object Fourth : NavAppLandingScreens("Fourth")
}

sealed class NavInAppScreens(override val routeName: String) : BaseRoutes() {
    object RootNesned: NavInAppScreens("NavAuthScreen")
    object LoginScreen : NavInAppScreens("LoginScreen")
    object RegisterScreen : NavInAppScreens("RegisterScreen")
    object HomeScreen : NavInAppScreens("HomeScreen") {
        var token = "token"
        val screenWithArgs = "${routeName}/{$token}"
        val arguments = listOf(
            navArgument(token) {
                NavType.StringType
                nullable = true
            }
        )
    }
}
