package com.patronusstudio.sisecevirmece2.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.patronusstudio.sisecevirmece2.NavInAppScreens
import com.patronusstudio.sisecevirmece2.nav.NesnedRoutes.passToInAppRoute

@Composable
fun ScreenHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavInAppScreens.RootNesned.routeName
    ) {
//        composable(route = NavSplashScreen.SplashScreens.routeName) {
//            SplashScreen {
//                navController.navigate(NavInAppScreens.RootNesned.routeName)
//            }
//        }
        //this.passToAppLandingRoute(navController)
        this.passToInAppRoute(navController)
    }
}