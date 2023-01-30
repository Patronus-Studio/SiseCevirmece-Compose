package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.data.viewModels.PackageViewModel
import com.patronusstudio.sisecevirmece.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece.ui.widgets.PackageDetailCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StoreScreen(back: () -> Unit) {
    val viewModel = hiltViewModel<PackageViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val titles = viewModel.categories.collectAsState().value
    val packageCardWidth = (LocalConfiguration.current.screenWidthDp * 0.9).dp
    val packageCardHeight = (LocalConfiguration.current.screenHeightDp * 0.1).dp
    val localContext = LocalContext.current
    val popupStatu = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        viewModel.getPackageCategories()
        viewModel.getPackageFromCategory( 1)
    }
    val clickedPackage = { item: PackageModel ->
        viewModel.setPackageModel(item)
        popupStatu.value = true
    }
    BaseBackground(titleId = R.string.store, backClicked = { back() }, contentOnTitleBottom = {
        Column(modifier = Modifier.fillMaxSize()) {
            PackageTitles(titles) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.clickedBtn(it)
                    viewModel.getPackageFromCategory(it)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewModel.packages.size) { index ->
                    Spacer(modifier = Modifier.height(16.dp))
                    PackagesCard(
                        cardWidth = packageCardWidth,
                        cardHeight = packageCardHeight,
                        packageModel = viewModel.packages[index],
                        clicked = clickedPackage
                    )
                }
            }
        }
        if (popupStatu.value) {
            PackagePopup(viewModel.currentPackage.collectAsState().value!!, dismissListener = {
                popupStatu.value = popupStatu.value.not()
            }, clickedBtn = {
                coroutineScope.launch(Dispatchers.Main) {
                    when (viewModel.currentPackage.value!!.packageStatu) {
                        PackageDetailCardBtnEnum.NEED_DOWNLOAD -> viewModel.downloadPackage()
                        PackageDetailCardBtnEnum.NEED_UPDATE -> viewModel.updatePackage()
                        PackageDetailCardBtnEnum.REMOVABLE -> viewModel.removePackage()
                    }
                }
            })
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    })
}

@Composable
fun PackagePopup(packageModel: PackageModel, dismissListener: () -> Unit, clickedBtn: () -> Unit) {
    val roundedCornerShape = RoundedCornerShape(16.dp)
    val popupProperties = PopupProperties(focusable = true)
    Popup(
        alignment = Alignment.BottomCenter,
        properties = popupProperties,
        onDismissRequest = dismissListener
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .background(Color.White, roundedCornerShape)
                .clip(roundedCornerShape)
        ) {
            PackageDetailCard(
                packageModel,
                packageDetailCardBtnEnum = packageModel.packageStatu,
                clickedBtn
            )
        }
    }
}

@Composable
fun PackageTitles(list: List<PackageCategoryModel>, clicked: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(content = {
            items(
                items = list
            ) { item: PackageCategoryModel ->
                Spacer(modifier = Modifier.width(16.dp))
                PackageSelectButton(item, content = {
                    val textColor = Color(
                        android.graphics.Color.parseColor(
                            if (item.isSelected == SelectableEnum.YES) item.activeTextColor
                            else item.passiveTextColor
                        )
                    )
                    Text(text = item.name, color = textColor)
                }, clicked = {
                    clicked((item.id).toInt())
                })
            }
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
        })
    }
}

@Composable
fun PackageSelectButton(item: BaseCategoryModel, content:(@Composable ()->Unit)? = null, clicked: () -> Unit) {
    val backgroundColor = Color(
        android.graphics.Color.parseColor(
            if (item.isSelected == SelectableEnum.YES) item.activeBtnColor
            else item.passiveBtnColor
        )
    )
    val borderColor = Color(
        android.graphics.Color.parseColor(
            if (item.isSelected == SelectableEnum.YES) item.passiveBtnColor
            else item.activeBtnColor
        )
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = clicked)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if(content != null) content()
    }
}

@Composable
fun PackagesCard(
    cardWidth: Dp,
    cardHeight: Dp,
    packageModel: PackageModel,
    clicked: (PackageModel) -> Unit
) {
    val cornerShape = RoundedCornerShape(16.dp)
    ConstraintLayout(modifier = Modifier.wrapContentSize()) {
        val (cardReference, statuReference) = createRefs()
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .background(Color.LightGray, cornerShape)
                .clip(cornerShape)
                .constrainAs(cardReference) {
                    this.bottom.linkTo(parent.bottom)
                }
                .clickable {
                    clicked(packageModel)
                }, contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = packageModel.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.Green), CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = packageModel.packageName,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Text(
                        text = packageModel.username,
                        fontSize = 10.sp,
                        maxLines = 1,
                        style = TextStyle(color = AppColor.DavysGrey)
                    )
                }
            }
        }
        packageModel.imageId?.let {
            Image(painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(statuReference) {
                        this.top.linkTo(cardReference.top, margin = 16.dp)
                        this.end.linkTo(cardReference.end, margin = 8.dp)
                        this.bottom.linkTo(cardReference.top)
                    })
        }
    }
}














