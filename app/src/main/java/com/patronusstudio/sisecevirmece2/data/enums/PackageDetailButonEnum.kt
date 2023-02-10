package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor

enum class PackageDetailButtonEnum {
    SHOW_QUESTION {
        override fun getBackgroundColor(): Color = AppColor.UnitedNationsBlue
        override fun getContent(context: Context): String =
            context.getString(R.string.show_question)
    },
    REMOVE_PACKAGE{
        override fun getBackgroundColor(): Color = AppColor.SunsetOrange
        override fun getContent(context: Context): String =
            context.getString(R.string.remove_package)
    };

    abstract fun getBackgroundColor(): Color
    abstract fun getContent(context: Context): String
}