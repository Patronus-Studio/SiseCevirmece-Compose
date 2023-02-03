package com.patronusstudio.sisecevirmece2.ui.views.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.QuestionModel
import com.patronusstudio.sisecevirmece2.data.utils.resize
import com.patronusstudio.sisecevirmece2.data.viewModels.AddCategoriesScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece2.ui.widgets.ButtonWithDot
import com.patronusstudio.sisecevirmece2.ui.widgets.ButtonWithPassive
import com.patronusstudio.sisecevirmece2.ui.widgets.ErrorSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCategoriesScreen(back: () -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    val questionCardMaxHeight = LocalConfiguration.current.screenHeightDp * 0.55
    val questionCardMaxWidth = width * 0.9
    val scaledImageSize = (questionCardMaxWidth * 0.6).roundToInt()
    val dotButtonHeight = LocalConfiguration.current.screenHeightDp * 0.07
    val viewModel = hiltViewModel<AddCategoriesScreenViewModel>()
    val localContext = LocalContext.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (Build.VERSION.SDK_INT < 28) {
                viewModel.setBitmap(
                    MediaStore.Images.Media.getBitmap(
                        localContext.contentResolver,
                        result
                    ).resize(scaledImageSize, scaledImageSize)
                )
            } else {
                val source = ImageDecoder.createSource(localContext.contentResolver, result!!)
                viewModel.setBitmap(
                    ImageDecoder.decodeBitmap(source).resize(scaledImageSize, scaledImageSize)
                )
            }
        }
    BackHandler {
        if (sheetState.isVisible) {
            viewModel.clearErrorMessage()
        } else {
            back()
        }
    }
    LaunchedEffect(key1 = viewModel.errorMessage.collectAsState().value) {
        if (viewModel.errorMessage.value.isNotEmpty()) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            viewModel.getPackageCategories()
        }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {
            ErrorSheet(message = viewModel.errorMessage.collectAsState().value) {
                viewModel.clearErrorMessage()
            }
        },
        sheetState = sheetState
    ) {
        BaseBackground(titleId = R.string.add_category, backClicked = { back() }, contentOnTitleBottom = {
            CategoryCard(
                questionCardMaxWidth,
                viewModel.packageName.collectAsState().value,
                viewModel.packageComment.collectAsState().value,
                viewModel.selectedImage.collectAsState().value,
                packageNameListener = {
                    viewModel.setPackageName(it)
                }, packageCommentListener = {
                    viewModel.setPackageComment(it)
                }, selectImageClicked = {
                    galleryLauncher.launch("image/*")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            CategoryType(viewModel = viewModel, dotButtonHeight = dotButtonHeight)
            Spacer(modifier = Modifier.height(8.dp))
            QuestionsCard(questionCardMaxHeight, questionCardMaxWidth, viewModel)
            AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
                LoadingAnimation()
            }
        })
    }
}

@Composable
private fun CategoryCard(
    questionCardMaxWidth: Double, packageName: String, packageComment: String,
    bitmap: Bitmap?, packageNameListener: (String) -> Unit,
    packageCommentListener: (String) -> Unit,
    selectImageClicked: () -> Unit
) {
    val addImageCardSize = (questionCardMaxWidth * 0.3).dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            Modifier
                .width(questionCardMaxWidth.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            AddImage(addImageCardSize, bitmap, selectImageClicked)
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.wrapContentSize()) {
                CategoryTextField(
                    text = packageName,
                    placeText = stringResource(R.string.enter_package_name), packageNameListener
                )
                CategoryTextField(
                    text = packageComment,
                    placeText = stringResource(R.string.enter_package_comment),
                    packageCommentListener
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun CategoryType(
    viewModel: AddCategoriesScreenViewModel,
    dotButtonHeight: Double
) {
    val userSelectedCategory = remember { mutableStateOf(0) }
    LazyRow {
        items(items = viewModel.categories.value,
            itemContent = { item: PackageCategoryModel ->
                AnimatedVisibility(visible = item.id.toInt() == userSelectedCategory.value) {
                    ButtonWithDot(
                        height = dotButtonHeight.toInt(),
                        dotColor = AppColor.Green,
                        btnColor = Color(android.graphics.Color.parseColor(item.activeBtnColor)),
                        text = item.name,
                        textColor = Color(android.graphics.Color.parseColor(item.activeTextColor))
                    ) {
                        userSelectedCategory.value = item.id.toInt()
                    }
                }
                AnimatedVisibility(visible = item.id.toInt() != userSelectedCategory.value) {
                    ButtonWithPassive(
                        height = dotButtonHeight.toInt(),
                        btnColor = Color(android.graphics.Color.parseColor(item.passiveBtnColor)),
                        text = item.name,
                        textColor = Color(android.graphics.Color.parseColor(item.passiveTextColor))
                    ) {
                        viewModel.setPackageCategory(item)
                        userSelectedCategory.value = item.id.toInt()
                    }
                }
            })
    }

}

@Composable
private fun QuestionsCard(
    questionCardMaxHeight: Double,
    questionCardMaxWidth: Double,
    viewModel: AddCategoriesScreenViewModel
) {
    val localFocus = LocalFocusManager.current
    val localContext = LocalContext.current
    var btnAddLocationX by remember { mutableStateOf(0.dp) }
    val offsetStateAddBtn =
        animateDpAsState(targetValue = btnAddLocationX, animationSpec = tween(1000))
    var btnRemoveLocationX by remember { mutableStateOf(0.dp) }
    val offsetStateRemoveBtn =
        animateDpAsState(targetValue = btnRemoveLocationX, animationSpec = tween(1000))
    val list = viewModel.questionList.collectAsState().value
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    GetCardWithConstraint(questionCardMaxHeight, questionCardMaxWidth, questions = {
        LazyColumn(content = {
            this.items(list) { item ->
                QuestionViewItem(questionModel = item, valueChange = {
                    viewModel.updateQuestionModelText(item, it)
                }, removeBtnClicked = {
                    if (list.size > 1) {
                        if (list.size - 1 <= 9) {
                            btnAddLocationX = 0.dp
                            btnRemoveLocationX = 0.dp
                        } else {
                            btnAddLocationX = (-80).dp
                            btnRemoveLocationX = 80.dp
                        }
                        coroutineScope.launch {
                            delay(500L)
                            viewModel.removeQuestionModel(item)
                        }

                    }
                })
                if (item.id == list.last().id) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }, state = listState)
    }, butons = {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .offset(x = offsetStateAddBtn.value)
        ) {
            CircleImageButton(id = R.drawable.add) {
                viewModel.addNewQuestionModel()
                if (list.size > 9) {
                    btnAddLocationX = (-80).dp
                    btnRemoveLocationX = 80.dp
                } else {
                    btnAddLocationX = 0.dp
                    btnRemoveLocationX = 0.dp
                }
                coroutineScope.launch {
                    listState.scrollToItem(list.size)
                }
            }
        }
        AnimatedVisibility(
            visible = list.size > 9,
            exit = fadeOut(tween(delayMillis = 100)),
            enter = fadeIn(tween(delayMillis = 100)),
            modifier = Modifier.offset(x = offsetStateRemoveBtn.value)
        ) {
            CircleImageButton(id = R.drawable.save) {
                coroutineScope.launch {
                    viewModel.saveQuestions(localContext)
                }
                localFocus.clearFocus()
            }
        }
    })
}

@Composable
private fun CircleImageButton(@DrawableRes id: Int, clicked: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "", modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable {
                clicked()
            }
    )
}


@Composable
fun AddImage(size: Dp, bitmap: Bitmap?, selectImageClicked: () -> Unit) {
    val gradients = listOf(
        AppColor.SeaSerpent,
        AppColor.SunsetOrange,
        AppColor.Mustard,
        AppColor.UnitedNationsBlue
    )
    val cornerShape16 = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .size(size)
            .clip(cornerShape16)
            .border(2.dp, Brush.horizontalGradient(gradients), cornerShape16)
            .background(Color.White)
            .clickable(onClick = selectImageClicked),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap == null) {
            Image(
                painter = painterResource(id = R.drawable.select_image),
                contentDescription = "", modifier = Modifier.size(80.dp)
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = bitmap),
                contentDescription = null, modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun CategoryTextField(text: String, placeText: String, valueChange: (String) -> Unit) {
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = AppColor.Purple200, textColor = AppColor.DavysGrey
    )
    TextField(
        value = text,
        singleLine = true,
        onValueChange = {
            valueChange(it)
        },
        placeholder = {
            Text(text = placeText, color = AppColor.DavysGrey)
        },
        colors = textFieldColors,
        modifier = Modifier.padding(end = 16.dp, start = 8.dp)
    )
}

@Composable
fun QuestionViewItem(
    modifier: Modifier = Modifier, questionModel: QuestionModel,
    valueChange: (String) -> Unit, removeBtnClicked: () -> Unit
) {
    val deviceWithSize = LocalConfiguration.current.screenWidthDp
    val widthSize = (deviceWithSize * 0.9).dp
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = AppColor.Purple200, textColor = AppColor.DavysGrey
    )
    Row(
        modifier = modifier
            .width(widthSize)
            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = questionModel.id.toString(),
            modifier = modifier.fillMaxWidth(0.05f),
            style = TextStyle().copy(textAlign = TextAlign.Center),
            color = AppColor.DavysGrey
        )
        TextField(
            value = questionModel.question,
            onValueChange = {
                valueChange(it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.enter_question),
                    color = AppColor.DavysGrey
                )
            },
            colors = textFieldColors,
            singleLine = true,
            modifier = modifier.fillMaxWidth(0.7f),
        )
        Box(
            modifier = modifier
                .fillMaxSize(0.2f)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    removeBtnClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = "",
                modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun GetCardWithConstraint(
    cardMaxHeightSize: Double,
    cardMaxWidthSize: Double,
    questions: @Composable () -> Unit,
    butons: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.TopCenter) {
        ConstraintLayout(
            modifier = Modifier
                .width(cardMaxWidthSize.dp)
                .wrapContentHeight()
                .padding(bottom = 16.dp)
                .imePadding()
        ) {
            val (cardRef, addBtnRef) = createRefs()
            Box(
                modifier = Modifier
                    .requiredHeightIn(100.dp, cardMaxHeightSize.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .animateContentSize(tween(500))
                    .constrainAs(cardRef) {
                        this.top.linkTo(parent.top)
                        this.centerHorizontallyTo(parent)
                    }
                    .padding(bottom = 8.dp)
            ) {
                Box(contentAlignment = Alignment.TopCenter) {
                    questions()
                }
            }
            Box(
                modifier = Modifier
                    .constrainAs(addBtnRef) {
                        this.top.linkTo(cardRef.bottom)
                        this.bottom.linkTo(cardRef.bottom)
                    }
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                butons()
            }
        }
    }
}
