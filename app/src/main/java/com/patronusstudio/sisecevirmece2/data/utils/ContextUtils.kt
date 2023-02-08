package com.patronusstudio.sisecevirmece2.data.utils

import android.content.Context
import android.widget.Toast

fun Context.showSample() {
    Toast.makeText(
        this,
        "Reklam yüklenirken hata oluştu",
        Toast.LENGTH_SHORT
    ).show()
}
