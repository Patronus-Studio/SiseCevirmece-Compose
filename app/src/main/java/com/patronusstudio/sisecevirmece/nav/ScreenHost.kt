package com.patronusstudio.sisecevirmece.data

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.patronusstudio.sisecevirmece.NavHomeScreen
import com.patronusstudio.sisecevirmece.NavLoginScreen
import com.patronusstudio.sisecevirmece.NavRegisterScreen
import com.patronusstudio.sisecevirmece.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece.ui.screens.HomeScreen
import com.patronusstudio.sisecevirmece.ui.screens.LoginScreen
import com.patronusstudio.sisecevirmece.ui.screens.RegisterScreen

@Composable
fun ScreenHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavLoginScreen.screenName) {
        val screenEnterAnimation = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in)
            .setExitAnim(android.R.anim.fade_out).build()
        composable(route = NavLoginScreen.screenName) { navBackStackEntry ->
            LoginScreen { loginEnum, token ->
                when (loginEnum) {
                    LoginScreenNavEnums.LOGIN -> {
                        navController.navigate(
                            NavHomeScreen.screenName + "/{$token}", navOptions {
                                popUpTo(0)
                            }
                        )
                    }
                    LoginScreenNavEnums.REGISTER -> navController.navigate(
                        NavRegisterScreen.screenName,
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
        ) { navBackStackEntry ->
            val token = navBackStackEntry.arguments?.getString(NavHomeScreen.token)
            HomeScreen(token ?: "")
        }
        composable(route = NavRegisterScreen.screenName){
            RegisterScreen()
        }
    }
}