package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece2.R

enum class TruthDareEnum {

    TRUTH {
        override fun getText(context: Context): String = context.getString(R.string.truth)
        override fun getImageId(): Int = R.drawable.truth
    },
    DARE {
        override fun getText(context: Context): String = context.getString(R.string.dare)
        override fun getImageId(): Int = R.drawable.dare
    },
    NOT_SELECTED {
        override fun getText(context: Context): String = context.getString(R.string.not_selected)
        override fun getImageId(): Int = R.drawable.bottle_sample
    };

    abstract fun getText(context: Context): String
    abstract fun getImageId(): Int
}