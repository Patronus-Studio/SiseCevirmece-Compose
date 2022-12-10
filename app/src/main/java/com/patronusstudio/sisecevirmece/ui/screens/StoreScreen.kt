package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.data.enums.PackageTitlesButonEnum
import com.patronusstudio.sisecevirmece.data.viewModels.PackageViewModel
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet

@Composable
fun StoreScreen() {

    val viewModel = hiltViewModel<PackageViewModel>()
    val packageCategories = viewModel.packageCategories.collectAsState().value?.packageCategoryModel

    LaunchedEffect(Unit) {
        viewModel.getPackageCategories()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PackageTitles()
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    }
}

@Preview
@Composable
fun PackageTitles() {
    val activeBtn = remember {
        mutableStateOf(true)
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActivePassiveButton(isActive = activeBtn.value, packageTitlesEnum = PackageTitlesButonEnum.CREATED) {
                if(it == PackageTitlesButonEnum.CREATED) activeBtn.value = !activeBtn.value
            }
            ActivePassiveButton(
                isActive = activeBtn.value.not(),
                packageTitlesEnum = PackageTitlesButonEnum.DOWNLOADED
            ) {
                if(it == PackageTitlesButonEnum.DOWNLOADED) activeBtn.value = !activeBtn.value
            }
        }
    }
}

@Composable
fun ActivePassiveButton(
    isActive: Boolean,
    packageTitlesEnum: PackageTitlesButonEnum,
    clicked: (PackageTitlesButonEnum) -> Unit
) {
    Button(
        onClick = { clicked(packageTitlesEnum) },
        colors = ButtonDefaults.buttonColors(
            if (isActive) packageTitlesEnum.activeBackgrounColor()
            else packageTitlesEnum.passiveBackgrounColor()
        )
    ) {
        Text(
            text = packageTitlesEnum.getTitles(),
            color = if (isActive) packageTitlesEnum.activeButonText()
            else packageTitlesEnum.passiveButtonText()
        )
    }

}

















