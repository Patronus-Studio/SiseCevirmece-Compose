package com.patronusstudio.sisecevirmece.data

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.patronusstudio.sisecevirmece.NavHomeScreen
import com.patronusstudio.sisecevirmece.NavLoginScreen
import com.patronusstudio.sisecevirmece.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece.ui.screens.HomeScreen
import com.patronusstudio.sisecevirmece.ui.screens.LoginScreen

@Composable
fun ScreenHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavLoginScreen.screenName) {
        val screenEnterAnimation = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in)
            .setExitAnim(android.R.anim.fade_out).build()
        composable(route = NavLoginScreen.screenName) { navBackStackEntry ->
            LoginScreen {
                when (it) {
                    LoginScreenNavEnums.LOGIN -> {
                        navController.navigate(
                            NavHomeScreen.screenName + "/Süleyman/Sezer",
                            screenEnterAnimation
                        )
                    }
                    LoginScreenNavEnums.REGISTER -> navController.navigate(
                        NavHomeScreen.screenWithArgs,
                        screenEnterAnimation
                    )
                    LoginScreenNavEnums.FORGET_PASSWORD -> navController.navigate(
                        NavHomeScreen.screenWithArgs,
                        screenEnterAnimation
                    )
                }
            }
        }
        composable(
            route = NavHomeScreen.screenWithArgs,
            arguments = NavHomeScreen.arguments
        )
        { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString(NavHomeScreen.name)
            val surname = navBackStackEntry.arguments?.getString(NavHomeScreen.surname)
            HomeScreen(name ?: "")
        }
    }

}