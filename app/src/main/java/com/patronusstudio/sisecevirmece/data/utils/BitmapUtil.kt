package com.patronusstudio.sisecevirmece.data.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


fun Bitmap.toByteArrray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 80, stream)
    return stream.toByteArray()
}

fun Bitmap.compress(): Bitmap {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    return this
}
