package com.patronusstudio.sisecevirmece.nav.NesnedRoutes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.patronusstudio.sisecevirmece.NavInAppScreens
import com.patronusstudio.sisecevirmece.data.enums.InAppScreenNavEnums
import com.patronusstudio.sisecevirmece.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece.ui.screens.*

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
                when(it){
                    InAppScreenNavEnums.LOGOUT -> {
                        navController.popBackStack()
                        navController.navigate(NavInAppScreens.LoginScreen.routeName)
                    }
                    InAppScreenNavEnums.STORES -> {
                        navController.navigate(NavInAppScreens.StoreScreen.routeName)
                    }
                    InAppScreenNavEnums.PLAY_GAME -> {

                    }
                    InAppScreenNavEnums.ADD_CATEGORIES -> {
                        navController.navigate(NavInAppScreens.AddCategoriesScreen.routeName)
                    }
                }
            }
        }
        composable(route = NavInAppScreens.StoreScreen.routeName) {
            StoreScreen {
                navController.popBackStack()
            }
        }
        composable(route = NavInAppScreens.AddCategoriesScreen.routeName){
            AddCategoriesScreen {
                navController.popBackStack()
            }
        }
    }

}