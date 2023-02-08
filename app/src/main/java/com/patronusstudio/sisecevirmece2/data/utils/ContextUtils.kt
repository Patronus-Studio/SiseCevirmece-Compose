package com.patronusstudio.sisecevirmece2.data.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.showSample(message:String? = null) {
    Toast.makeText(
        this,
        message ?: "Reklam yüklenirken hata oluştu",
        Toast.LENGTH_SHORT
    ).show()
}

fun showLog(message: String){
    Log.d("Sülo",message)
}