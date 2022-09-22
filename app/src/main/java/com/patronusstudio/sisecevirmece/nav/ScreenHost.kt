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
        composable(route = NavLoginScreen.screenName) {
            LoginScreen {
                when (it) {
                    LoginScreenNavEnums.LOGIN -> navController.navigate(
                        NavHomeScreen.screenName,
                        screenEnterAnimation
                    )
                    LoginScreenNavEnums.REGISTER -> navController.navigate(
                        NavHomeScreen.screenName,
                        screenEnterAnimation
                    )
                    LoginScreenNavEnums.FORGET_PASSWORD -> navController.navigate(
                        NavHomeScreen.screenName,
                        screenEnterAnimation
                    )
                }
            }
        }
        composable(route = NavHomeScreen.screenName) {
            HomeScreen()
        }
    }

}