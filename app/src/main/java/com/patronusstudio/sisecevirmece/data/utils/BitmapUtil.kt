package com.patronusstudio.sisecevirmece.data.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream


fun Bitmap.toByteArrray(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(compressFormat, 50, stream)
    return stream.toByteArray()
}

fun Bitmap.compress(): Bitmap {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return this
}

fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
    return if (maxHeight > 0 && maxWidth > 0) {
        val width = this.width
        val height = this.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        Bitmap.createScaledBitmap(this, finalWidth, finalHeight, true)
    } else this
}

fun  Int.toBitmapArray(context:Context): ByteArray{
    val bitmapImage = Glide.with(context).asBitmap().load(this).centerInside().submit().get()
    return bitmapImage.toByteArrray(Bitmap.CompressFormat.PNG)
}