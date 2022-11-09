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
            Buttonsss(isActive = activeBtn.value, packageTitlesEnum = PackageTitlesEnum.CREATED) {
                if(it == PackageTitlesEnum.CREATED) activeBtn.value = !activeBtn.value
            }
            Buttonsss(
                isActive = activeBtn.value.not(),
                packageTitlesEnum = PackageTitlesEnum.DOWNLOADED
            ) {
                if(it == PackageTitlesEnum.DOWNLOADED) activeBtn.value = !activeBtn.value
            }
        }
    }
}

@Composable
fun Buttonsss(
    isActive: Boolean,
    packageTitlesEnum: PackageTitlesEnum,
    clicked: (PackageTitlesEnum) -> Unit
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

enum class PackageTitlesEnum {
    CREATED {
        override fun getTitles() = "Oluşturduklarım"

        override fun activeBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))

        override fun activeButonText(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveButtonText(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))
    },
    DOWNLOADED {
        override fun getTitles() = "İndirdiklerim"

        override fun activeBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))

        override fun activeButonText(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveButtonText(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))
    };

    abstract fun getTitles(): String
    abstract fun activeBackgrounColor(): Color
    abstract fun passiveBackgrounColor(): Color
    abstract fun activeButonText(): Color
    abstract fun passiveButtonText(): Color
}
















