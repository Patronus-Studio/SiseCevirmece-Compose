package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronusstudio.sisecevirmece.data.enums.DataFetchStatus
import com.patronusstudio.sisecevirmece.data.viewModels.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(isLogged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val viewModel = viewModel<SplashScreenViewModel>()
    val dataFetchStatus by viewModel.dataFetchStatus.collectAsState()
    val userToken by viewModel.userToken.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getToken(context)
        while (dataFetchStatus != DataFetchStatus.FINISHED) {
            delay(100)
        }
        isLogged(userToken != null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Text(text = "Splash Screen")
    }
}