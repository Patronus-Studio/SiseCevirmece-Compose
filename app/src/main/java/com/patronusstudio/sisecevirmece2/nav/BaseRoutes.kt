package com.patronusstudio.sisecevirmece2

sealed class BaseRoutes {
    abstract val routeName: String
}

sealed class NavSplashScreen(override val routeName: String) : BaseRoutes() {
    object SplashScreens : NavSplashScreen("NavSplashScreen")
}

sealed class NavAppLandingScreens(override val routeName: String) : BaseRoutes() {
    object RootNesned : NavAppLandingScreens("NavAppLandingScreens")
    object First : NavAppLandingScreens("First")
    object Second : NavAppLandingScreens("Second")
    object Third : NavAppLandingScreens("Third")
    object Fourth : NavAppLandingScreens("Fourth")
}

sealed class NavInAppScreens(override val routeName: String) : BaseRoutes() {
    object RootNesned : NavInAppScreens("NavAuthScreen")
    object LoginScreen : NavInAppScreens("LoginScreen")
    object RegisterScreen : NavInAppScreens("RegisterScreen")
    object HomeScreen : NavInAppScreens("HomeScreen")
    object StoreScreen : NavInAppScreens("StoreScreen")
    object AddCategoriesScreen : NavInAppScreens("AddCategoriesScreen")
    object GameTypeSelection : NavInAppScreens("GameTypeSelection")
    object NormalGameScreen : NavInAppScreens("NormalGameScreen")
    object SpecialGameCategorySelectScreen : NavInAppScreens("SpecialGameCategorySelectScreen")
    object SpecialGameScreen : NavInAppScreens("SpecialGameScreen")
    object ProfileScreen : NavInAppScreens("ProfileScreen")
}
