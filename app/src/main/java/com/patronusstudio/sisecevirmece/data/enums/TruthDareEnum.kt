package com.patronusstudio.sisecevirmece.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece.R

enum class TruthDareEnum {

    TRUTH {
        override fun getText(context: Context): String = context.getString(R.string.truth)
        override fun getImageId(): Int = R.drawable.truth
    },
    DARE {
        override fun getText(context: Context): String = context.getString(R.string.dare)
        override fun getImageId(): Int = R.drawable.dare
    };

    abstract fun getText(context: Context): String
    abstract fun getImageId(): Int
}