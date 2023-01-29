package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.ProfileCategoryModel
import com.patronusstudio.sisecevirmece.data.viewModels.ProfileScreenViewModel
import com.patronusstudio.sisecevirmece.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece.ui.widgets.SampleCard
import com.patronusstudio.sisecevirmece.ui.widgets.SampleTempCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(backClicked: () -> Unit) {

    val viewModel = hiltViewModel<ProfileScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val packageCardWidth = (LocalConfiguration.current.screenWidthDp * 0.4).dp
    val packageCardHeight = (LocalConfiguration.current.screenHeightDp * 0.25).dp
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getPackages()
    })

    BaseBackground(titleId = R.string.my_profile, backClicked = backClicked) {
        Titles(list = viewModel.titles.collectAsState().value) {
            viewModel.clickedBtn(it.id)
            viewModel.setTitle(it)
            coroutineScope.launch {
                viewModel.getDatas(it)
            }
        }
        AnimatedVisibility(
            visible = viewModel.packages.collectAsState().value.isNotEmpty(),
            enter = fadeIn() + slideInVertically(), exit = fadeOut()
        ) {
            Bottles(viewModel.packages.collectAsState().value,packageCardWidth, packageCardHeight)
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    }
}

@Composable
private fun Titles(list: List<BaseCategoryModel>, clicked: (ProfileCategoryModel) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(
                    items = list
                ) { item: BaseCategoryModel ->
                    Spacer(modifier = Modifier.width(16.dp))
                    PackageSelectButton(item = item, clicked = {
                        clicked(item as ProfileCategoryModel)
                    }, content = {
                        item as ProfileCategoryModel
                        val textColor = Color(
                            android.graphics.Color.parseColor(
                                if (item.isSelected == SelectableEnum.YES) item.activeTextColor
                                else item.passiveTextColor
                            )
                        )
                        Text(text = item.name, color = textColor)
                    })

                }
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Bottles(packages: List<PackageDbModel>, packageCardWidth: Dp, packageCardHeight: Dp) {
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        packages.forEachIndexed { index, packageDbModel ->
            SampleCard(
                width = packageCardWidth,
                height = packageCardHeight,
                model = packageDbModel
            )
            if (index == packages.size - 1) {
                SampleTempCard(packageCardWidth, packageCardHeight)
            }
        }
    }

}
