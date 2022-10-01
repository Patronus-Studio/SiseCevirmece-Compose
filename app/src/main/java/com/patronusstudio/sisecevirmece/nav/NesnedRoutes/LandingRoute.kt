package com.patronusstudio.sisecevirmece.nav.NesnedRoutes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patronusstudio.sisecevirmece.NavAppLandingScreens
import com.patronusstudio.sisecevirmece.ui.screens.LandingFirstScreen

fun NavGraphBuilder.passToAppLandingRoute(navController: NavHostController) {
    navigation(
        startDestination = NavAppLandingScreens.First.routeName,
        route = NavAppLandingScreens.RootNesned.routeName
    ) {
        composable(route = NavAppLandingScreens.First.routeName) {
            LandingFirstScreen()
        }
    }
}