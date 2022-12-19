package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.YESORNO
import com.patronusstudio.sisecevirmece.data.viewModels.PackageViewModel
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StoreScreen() {

    val viewModel = hiltViewModel<PackageViewModel>()
    val titles = viewModel.categories.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getPackageCategories()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PackageTitles(titles) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.clickedBtn(it)
                    viewModel.getPackageFromCategory(it)
                }
            }
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    }
}

@Composable
fun PackageTitles(list: List<PackageCategoryModel>, clicked: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(content = {
            items(
                items = list,
                key = {
                    it.id
                }
            ) { item: PackageCategoryModel ->
                Spacer(modifier = Modifier.width(16.dp))
                Button(item) {
                    clicked(item.id - 1)
                }
            }
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
        })
    }
}

@Composable
fun Button(item: PackageCategoryModel, clicked: () -> Unit) {
    val backgroundColor = Color(
        android.graphics.Color.parseColor(
            if (item.isSelected == YESORNO.YES) item.activeBtnColor
            else item.passiveBtnColor
        )
    )
    val borderColor = Color(
        android.graphics.Color.parseColor(
            if (item.isSelected == YESORNO.YES) item.passiveBtnColor
            else item.activeBtnColor
        )
    )
    val textColor = Color(
        android.graphics.Color.parseColor(
            if (item.isSelected == YESORNO.YES) item.activeTextColor
            else item.passiveTextColor
        )
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { clicked() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.name, color = textColor)
    }
}

@Preview
@Composable
fun PackageCard() {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
    ) {
        Row(Modifier.fillMaxSize()) {
            Box(modifier = Modifier.size(50.dp)){
                Image(painter = rememberAsyncImagePainter(
                    model = null,
                    imageLoader = ImageLoader()
                ), contentDescription = )
            }
        }
    }
}














