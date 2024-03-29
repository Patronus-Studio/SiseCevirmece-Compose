package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece2.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece2.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.PackageModel
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.viewModels.PackageViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.AdmobInterstialAd
import com.patronusstudio.sisecevirmece2.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece2.ui.widgets.PackageDetailCard
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
    val isAdsShowing = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        val itemId = viewModel.getPackageCategories()
        viewModel.getPackageFromCategory(itemId)
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
                    viewModel.getPackageFromCategory(it.id.toInt())
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
            PackagePopupControl(viewModel, popupDissmissed = {
                popupStatu.value = popupStatu.value.not()
            }, adsLoaded = {
                isAdsShowing.value = true
            })
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    })
    if(isAdsShowing.value){
        AdmobInterstialAd(context = localContext,
            addUnitId = BuildConfig.package_download_interstitial,
            failedLoad = {
                coroutineScope.launch(Dispatchers.Main) {
                    packageStatus(viewModel)
                    viewModel.setLoadingStatus(false)
                    isAdsShowing.value = false
                }
            }, adClosed = {
                coroutineScope.launch(Dispatchers.Main) {
                    packageStatus(viewModel)
                    viewModel.setLoadingStatus(false)
                    isAdsShowing.value = false
                }
            })
    }
}

@Composable
private fun PackagePopupControl(
    viewModel: PackageViewModel,
    adsLoaded: () -> Unit,
    popupDissmissed: () -> Unit
) {
    PackagePopup(viewModel.currentPackage.collectAsState().value!!,
        dismissListener = {
            popupDissmissed()
        }, clickedBtn = {
            viewModel.setLoadingStatus(true)
            adsLoaded()
        })
}

@Composable
fun PackagePopup(packageModel: PackageModel, dismissListener: () -> Unit, clickedBtn: () -> Unit) {
    val popupProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
    Dialog(onDismissRequest = { dismissListener() }, popupProperties) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            PackageDetailCard(
                packageModel,
                packageDetailCardBtnEnum = packageModel.packageStatu,
                clickedBtn
            )
        }
    }
}

@Composable
fun PackageTitles(list: List<PackageCategoryModel>, clicked: (PackageCategoryModel) -> Unit) {
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
                    Text(
                        text = item.name,
                        color = textColor,
                        fontFamily = BetmRounded,
                        fontWeight = FontWeight.Normal
                    )
                }, clicked = {
                    clicked(item)
                })
            }
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
        })
    }
}

@Composable
fun PackageSelectButton(
    item: BaseCategoryModel,
    content: (@Composable () -> Unit)? = null,
    clicked: () -> Unit
) {
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
        if (content != null) content()
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
                val colorList = mutableListOf<Color>().apply {
                    this.add(Color(0xFFf09433))
                    this.add(Color(0xFFe6683c))
                    this.add(Color(0xffdc2743))
                    this.add(Color(0xFFcc2366))
                    this.add(Color(0xFFbc1888))
                }
                AsyncImage(
                    model = packageModel.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(
                                2.dp,
                                Brush.verticalGradient(colorList, tileMode = TileMode.Mirror)
                            ), CircleShape
                        )
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
                        color = AppColor.Black,
                        maxLines = 1, fontFamily = BetmRounded,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = packageModel.username,
                        fontSize = 10.sp,
                        maxLines = 1, color = AppColor.DavysGrey,
                        fontFamily = BetmRounded,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        packageModel.imageId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(statuReference) {
                        this.top.linkTo(cardReference.top, margin = 16.dp)
                        this.end.linkTo(cardReference.end, margin = 8.dp)
                        this.bottom.linkTo(cardReference.top)
                    }, contentScale = ContentScale.Crop
            )
        }
    }
}

private suspend fun packageStatus(viewModel: PackageViewModel) {

    when (viewModel.currentPackage.value!!.packageStatu) {
        PackageDetailCardBtnEnum.NEED_DOWNLOAD -> {
            viewModel.downloadPackage()
            viewModel.updateDownloadCountOnService()
        }

        PackageDetailCardBtnEnum.NEED_UPDATE -> {
            viewModel.updatePackage()
            viewModel.updateDownloadCountOnService()
        }

        PackageDetailCardBtnEnum.REMOVABLE -> viewModel.removePackage()
    }
}











