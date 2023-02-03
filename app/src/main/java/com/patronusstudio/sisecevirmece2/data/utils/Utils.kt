package com.patronusstudio.sisecevirmece2.data.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.patronusstudio.sisecevirmece2.data.model.PackageModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.checkEmailCorrect(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    val droppedLastBackspace = this.dropLastWhile {
        it == ' '
    }
    val pttn = "^\\D.+@.+\\.[a-z]+"
    val p = Pattern.compile(pttn)
    val m: Matcher = p.matcher(droppedLastBackspace)
    return m.matches()
}

fun isConnected(): Boolean {
    val command = "ping -c 1 google.com"
    return Runtime.getRuntime().exec(command).waitFor() == 0
}

fun PackageModel.toPackageDbModel(packageImage: ByteArray): PackageDbModel {
    return PackageDbModel(
        cloudPackageCategoryId = this.id,
        packageImage = packageImage,
        version = this.version,
        packageName = this.packageName,
        packageComment = this.packageComment,
        createdTime = this.createdTime,
        updatedTime = this.updatedTime
    )
}

suspend fun downloadImage(context: Context, downloadUrlOfImage: String?): Bitmap {
    return withContext(Dispatchers.IO) {
        Glide.with(context).asBitmap().load(downloadUrlOfImage).submit().get()
    }
}