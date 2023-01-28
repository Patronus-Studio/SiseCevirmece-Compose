package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.runtime.Composable
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground

@Composable
fun ProfileScreen(backClicked: () -> Unit) {

    BaseBackground(titleId = R.string.my_profile, backClicked = backClicked) {

    }
}