package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece2.R

enum class TruthDareDefaultPackageEnum {

    TRUTH {
        override fun getPackageName(context: Context): String = context.getString(R.string.truth)
        override fun getPackageComment(context: Context): String = context.getString(R.string.truth)
        override fun getQuestions(context: Context): Array<String> =
            context.resources.getStringArray(R.array.dogrulukListesi)

        override fun getImageId(): Int = R.drawable.truth
        override fun getVersion(): Int = 1
    },
    DARE {
        override fun getPackageName(context: Context): String = context.getString(R.string.dare)
        override fun getPackageComment(context: Context): String = context.getString(R.string.dare)
        override fun getQuestions(context: Context): Array<String> =
            context.resources.getStringArray(R.array.cesaretListesi)

        override fun getImageId(): Int = R.drawable.dare
        override fun getVersion(): Int = 1
    };

    abstract fun getPackageName(context: Context): String
    abstract fun getPackageComment(context: Context): String
    abstract fun getQuestions(context: Context): Array<String>
    abstract fun getImageId(): Int
    abstract fun getVersion(): Int

}