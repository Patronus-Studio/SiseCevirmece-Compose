package com.patronusstudio.sisecevirmece2.nav.NesnedRoutes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.patronusstudio.sisecevirmece2.MainApplication
import com.patronusstudio.sisecevirmece2.NavInAppScreens
import com.patronusstudio.sisecevirmece2.data.enums.GameMode
import com.patronusstudio.sisecevirmece2.data.enums.InAppScreenNavEnums
import com.patronusstudio.sisecevirmece2.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece2.ui.views.screens.AddCategoriesScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.GameTypeSelection
import com.patronusstudio.sisecevirmece2.ui.views.screens.HomeScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.LoginScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.NormalGameScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.ProfileScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.RegisterScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.SpecialGameCategorySelectScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.SpecialGameScreen
import com.patronusstudio.sisecevirmece2.ui.views.screens.StoreScreen

fun NavGraphBuilder.passToInAppRoute(navController: NavHostController) {
    navigation(
        startDestination = NavInAppScreens.LoginScreen.routeName,
        route = NavInAppScreens.RootNesned.routeName
    ) {
        composable(route = NavInAppScreens.RegisterScreen.routeName) {
            RegisterScreen {
                navController.navigate(
                    NavInAppScreens.HomeScreen.routeName, navOptions {
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
                            NavInAppScreens.HomeScreen.routeName, navOptions {
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
                when (it) {
                    InAppScreenNavEnums.LOGOUT -> {
                        MainApplication.authToken = ""
                        navController.popBackStack()
                        navController.navigate(NavInAppScreens.LoginScreen.routeName)
                    }

                    InAppScreenNavEnums.STORES -> {
                        navController.navigate(NavInAppScreens.StoreScreen.routeName)
                    }

                    InAppScreenNavEnums.PLAY_GAME -> {
                        navController.navigate(NavInAppScreens.GameTypeSelection.routeName)
                    }

                    InAppScreenNavEnums.ADD_CATEGORIES -> {
                        navController.navigate(NavInAppScreens.AddCategoriesScreen.routeName)
                    }

                    InAppScreenNavEnums.PROFILE -> {
                        navController.navigate(NavInAppScreens.ProfileScreen.routeName)
                    }

                    else -> ""
                }
            }
        }
        composable(route = NavInAppScreens.StoreScreen.routeName) {
            StoreScreen {
                navController.popBackStack()
            }
        }
        composable(route = NavInAppScreens.AddCategoriesScreen.routeName) {
            AddCategoriesScreen {
                navController.popBackStack()
            }
        }
        composable(route = NavInAppScreens.GameTypeSelection.routeName) {
            GameTypeSelection(back = { navController.popBackStack() }, gameModeSelection = {
                if (it == GameMode.NORMAL_MODE) navController.navigate(NavInAppScreens.NormalGameScreen.routeName)
                if (it == GameMode.SPECIAL_MODE) navController.navigate(NavInAppScreens.SpecialGameCategorySelectScreen.routeName)
            })
        }
        composable(route = NavInAppScreens.NormalGameScreen.routeName) {
            NormalGameScreen {
                navController.popBackStack()
            }
        }
        composable(route = NavInAppScreens.SpecialGameCategorySelectScreen.routeName) {
            SpecialGameCategorySelectScreen(
                backClicked = {
                    navController.popBackStack()
                }, passGameScreen = {
                    navController.currentBackStackEntry?.savedStateHandle.apply {
                        this?.set("selectedPackages", it)
                    }
                    navController.navigate(NavInAppScreens.SpecialGameScreen.routeName)
                })
        }
        composable(route = NavInAppScreens.SpecialGameScreen.routeName) {
            val list =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("selectedPackages")
            navController.previousBackStackEntry?.savedStateHandle?.remove<String>("selectedPackages")
            SpecialGameScreen(list ?: "") {
                navController.popBackStack()
            }
        }
        composable(route = NavInAppScreens.ProfileScreen.routeName) {
            ProfileScreen {
                navController.popBackStack()
            }
        }

    }

}