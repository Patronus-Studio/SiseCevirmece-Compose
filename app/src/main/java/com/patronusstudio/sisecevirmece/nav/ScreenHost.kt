package com.patronusstudio.sisecevirmece.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.patronusstudio.sisecevirmece.NavAppLandingScreens
import com.patronusstudio.sisecevirmece.NavInAppScreens
import com.patronusstudio.sisecevirmece.NavSplashScreen
import com.patronusstudio.sisecevirmece.nav.NesnedRoutes.passToAppLandingRoute
import com.patronusstudio.sisecevirmece.nav.NesnedRoutes.passToInAppRoute
import com.patronusstudio.sisecevirmece.ui.views.screens.SplashScreen

@Composable
fun ScreenHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavSplashScreen.SplashScreens.routeName
    ) {
        composable(route = NavSplashScreen.SplashScreens.routeName) {
            SplashScreen {
                if (it) navController.navigate(NavInAppScreens.RootNesned.routeName)
                else navController.navigate(NavAppLandingScreens.RootNesned.routeName)
            }
        }
        this.passToAppLandingRoute(navController)
        this.passToInAppRoute(navController)
    }
}