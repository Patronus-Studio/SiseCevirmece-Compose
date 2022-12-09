package com.patronusstudio.sisecevirmece.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.viewModels.AddCategoriesScreenViewModel
import com.patronusstudio.sisecevirmece.ui.theme.*
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle
import kotlinx.coroutines.launch

data class QuestionModel(
    override var id: Int,
    var question: String
) : BaseModelWithIndex() {
    override fun equals(other: Any?): Boolean = false
}

abstract class BaseModelWithIndex {
    abstract var id: Int
}

@Composable
@Preview
fun AddCategoriesScreen() {
    val width = LocalConfiguration.current.screenWidthDp
    val questionCardMaxHeight = LocalConfiguration.current.screenHeightDp * 0.6
    val questionCardMaxWidth = LocalConfiguration.current.screenWidthDp * 0.9
    val viewModel = hiltViewModel<AddCategoriesScreenViewModel>()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Heliotrope)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(stringResource(id = R.string.add_category)) {
            Toast.makeText(context, "Geri Tuşuna basıldı", Toast.LENGTH_SHORT).show()
        }
        CategoryCard(questionCardMaxWidth)
        Spacer(modifier = Modifier.height(16.dp))
        QuestionListView(questionCardMaxHeight, questionCardMaxWidth, viewModel)
    }
}

@Composable
private fun CategoryCard(questionCardMaxWidth: Double) {
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
            AddImage(addImageCardSize)
            Spacer(modifier = Modifier.width(8.dp))
            CategoryName()
            Spacer(modifier = Modifier.width(16.dp))
        }
    }

}

@Composable
private fun QuestionListView(
    questionCardMaxHeight: Double,
    questionCardMaxWidth: Double,
    viewModel: AddCategoriesScreenViewModel
) {
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
                    viewModel.removeQuestionModel(item)
                    if (viewModel.questionList.value.size <= 5) {
                        btnAddLocationX = 0.dp
                        btnRemoveLocationX = 0.dp
                    } else {
                        btnAddLocationX = (-80).dp
                        btnRemoveLocationX = 80.dp
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
                if (list.size > 5) {
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
        if (list.size > 5) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .offset(x = offsetStateRemoveBtn.value)
            ) {
                CircleImageButton(id = R.drawable.save) {

                }
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
fun AddImage(size: Dp) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            imageUri = result
        }

    val gradients = listOf(SeaSerpent, SunsetOrange, Mustard, UnitedNationsBlue)
    val cornerShape16 = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .size(size)
            .clip(cornerShape16)
            .border(2.dp, Brush.horizontalGradient(gradients), cornerShape16)
            .background(Color.White)
            .clickable {
                galleryLauncher.launch("image/*")
            }, contentAlignment = Alignment.Center

    ) {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                )
            }

        } ?: kotlin.run {
            Image(
                painter = painterResource(id = R.drawable.select_image),
                contentDescription = "", modifier = Modifier.size(80.dp)
            )

        }
    }
}

@Composable
fun CategoryName() {
    val packageName = remember { mutableStateOf("") }
    val cornerShape8 = RoundedCornerShape(8.dp)
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Purple200
    )
    TextField(
        value = packageName.value,
        singleLine = true,
        onValueChange = {
            packageName.value = it
        },
        placeholder = {
            Text(text = stringResource(R.string.enter_package_name))
        },
        colors = textFieldColors
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
        focusedIndicatorColor = Purple200
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
            style = TextStyle().copy(textAlign = TextAlign.Center)
        )
        TextField(
            value = questionModel.question,
            onValueChange = {
                valueChange(it)
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_question))
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
