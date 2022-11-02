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
            LoginScreen { loginEnum ->
                when (loginEnum) {
                    LoginScreenNavEnums.LOGIN -> {
                        navController.navigate(
                            NavInAppScreens.HomeScreen.routeName , navOptions {
                                popUpTo(0)
                            }
                        )
                    }
                    LoginScreenNavEnums.REGISTER -> navController.navigate(
                        NavInAppScreens.RegisterScreen.routeName
                    )
                    LoginScreenNavEnums.FORGET_PASSWORD -> navController.navigate(
                        NavInAppScreens.HomeScreen.routeName
                    )
                }
            }
        }
        composable(
            route = NavInAppScreens.HomeScreen.routeName
        ) { navBackStackEntry ->
            HomeScreen {
                navController.popBackStack()
                navController.navigate(NavInAppScreens.LoginScreen.routeName)
            }
        }

    }

}