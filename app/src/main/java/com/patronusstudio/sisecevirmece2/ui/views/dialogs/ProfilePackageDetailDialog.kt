package com.patronusstudio.sisecevirmece2.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.patronusstudio.sisecevirmece2.data.enums.PackageDetailButtonEnum
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.ui.widgets.SampleText

@Composable
fun ProfilePackageCard(
    packageDbModel: PackageDbModel,
    btnClickResult: (PackageDetailButtonEnum) -> Unit
) {
    val imageSize = 80.dp
    val buttonHeight = 50.dp
    val roundedCornerShape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, roundedCornerShape)
            .clip(roundedCornerShape)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(
                    packageDbModel.packageImage
                )
                    .crossfade(true)
                    .build(), contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.height(imageSize)
            ) {
                SampleText(content = packageDbModel.packageName, 1, 20)
                SampleText(
                    content = packageDbModel.packageComment,
                    3,
                    fontSize = 12
                )
            }
        }
        Spacer(modifier = Modifier.height(buttonHeight))
        ProfilePackageCardBtn(
            buttonHeight = buttonHeight,
            packageDetailButtonEnum = PackageDetailButtonEnum.SHOW_QUESTION,
            btnClickResult = btnClickResult
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfilePackageCardBtn(
            buttonHeight = buttonHeight,
            packageDetailButtonEnum = PackageDetailButtonEnum.REMOVE_PACKAGE,
            btnClickResult = btnClickResult
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProfilePackageCardBtn(
    buttonHeight: Dp,
    packageDetailButtonEnum: PackageDetailButtonEnum,
    btnClickResult: (PackageDetailButtonEnum) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = { btnClickResult(packageDetailButtonEnum) },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f)
                .height(buttonHeight),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = packageDetailButtonEnum.getBackgroundColor())
        ) {
            Text(text = packageDetailButtonEnum.getContent(LocalContext.current))
        }
    }
}
