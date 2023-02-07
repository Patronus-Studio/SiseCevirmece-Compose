package com.patronusstudio.sisecevirmece2.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patronusstudio.sisecevirmece2.NavAppLandingScreens
import com.patronusstudio.sisecevirmece2.NavInAppScreens
import com.patronusstudio.sisecevirmece2.NavSplashScreen
import com.patronusstudio.sisecevirmece2.nav.NesnedRoutes.passToAppLandingRoute
import com.patronusstudio.sisecevirmece2.nav.NesnedRoutes.passToInAppRoute
import com.patronusstudio.sisecevirmece2.ui.views.screens.SplashScreen

@Composable
fun ScreenHost(navController: NavHostController,mixpanelAPI: MixpanelAPI) {
    NavHost(
        navController = navController,
        startDestination = NavSplashScreen.SplashScreens.routeName
    ) {
        composable(route = NavSplashScreen.SplashScreens.routeName) {
            SplashScreen {
                navController.navigate(NavInAppScreens.RootNesned.routeName)
            }
        }
        //this.passToAppLandingRoute(navController)
        this.passToInAppRoute(navController,mixpanelAPI)
    }
}