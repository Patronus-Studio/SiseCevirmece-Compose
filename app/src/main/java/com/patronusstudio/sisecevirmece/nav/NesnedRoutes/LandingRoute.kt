package com.patronusstudio.sisecevirmece.nav.NesnedRoutes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patronusstudio.sisecevirmece.NavAppLandingScreens
import com.patronusstudio.sisecevirmece.NavInAppScreens
import com.patronusstudio.sisecevirmece.ui.screens.LandingFirstScreen
import com.patronusstudio.sisecevirmece.ui.screens.LandingLastScreen
import com.patronusstudio.sisecevirmece.ui.screens.LandingSecondScreen
import com.patronusstudio.sisecevirmece.ui.screens.LandingThirdScreen

fun NavGraphBuilder.passToAppLandingRoute(navController: NavHostController) {
    navigation(
        startDestination = NavAppLandingScreens.First.routeName,
        route = NavAppLandingScreens.RootNesned.routeName
    ) {
        composable(route = NavAppLandingScreens.First.routeName) {
            LandingFirstScreen {
                navController.navigate(NavAppLandingScreens.Second.routeName)
            }
        }
        composable(route = NavAppLandingScreens.Second.routeName) {
            LandingSecondScreen {
                navController.navigate(NavAppLandingScreens.Third.routeName)
            }
        }
        composable(route = NavAppLandingScreens.Third.routeName) {
            LandingThirdScreen {
                navController.navigate(NavAppLandingScreens.Fourth.routeName)
            }
        }
        composable(route = NavAppLandingScreens.Fourth.routeName) {
            LandingLastScreen {
                navController.navigate(NavInAppScreens.RootNesned.routeName)
            }
        }
    }
}