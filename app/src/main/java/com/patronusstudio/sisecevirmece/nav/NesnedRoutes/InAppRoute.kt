package com.patronusstudio.sisecevirmece.nav.NesnedRoutes

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.patronusstudio.sisecevirmece.NavInAppScreens
import com.patronusstudio.sisecevirmece.NavSplashScreen
import com.patronusstudio.sisecevirmece.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece.ui.screens.HomeScreen
import com.patronusstudio.sisecevirmece.ui.screens.LoginScreen
import com.patronusstudio.sisecevirmece.ui.screens.RegisterScreen

fun NavGraphBuilder.passToInAppRoute(navController: NavHostController) {
    navigation(
        startDestination = NavInAppScreens.LoginScreen.routeName,
        route = NavInAppScreens.RootNesned.routeName
    ) {
        composable(route = NavInAppScreens.RegisterScreen.routeName) {
            RegisterScreen { userToken ->
                navController.navigate(
                    NavInAppScreens.HomeScreen.routeName + "/{$userToken}", navOptions {
                        popUpTo(0)
                    }
                )
            }
        }
        composable(route = NavInAppScreens.LoginScreen.routeName) { navBackStackEntry ->
            LoginScreen { loginEnum, token ->
                when (loginEnum) {
                    LoginScreenNavEnums.LOGIN -> {
                        navController.navigate(
                            NavInAppScreens.HomeScreen.routeName + "/$token", navOptions {
                                popUpTo(0)
                            }
                        )
                    }
                    LoginScreenNavEnums.REGISTER -> navController.navigate(
                        NavInAppScreens.RegisterScreen.routeName
                    )
                    LoginScreenNavEnums.FORGET_PASSWORD -> navController.navigate(
                        NavInAppScreens.HomeScreen.screenWithArgs
                    )
                }
            }
        }
        composable(
            route = NavInAppScreens.HomeScreen.screenWithArgs,
            arguments = NavInAppScreens.HomeScreen.arguments
        ) { navBackStackEntry ->
            val token = navBackStackEntry.arguments?.getString(NavInAppScreens.HomeScreen.token)
            HomeScreen(token ?: "") {
                navController.popBackStack()
                navController.navigate(NavInAppScreens.LoginScreen.routeName)
            }
        }

    }

}