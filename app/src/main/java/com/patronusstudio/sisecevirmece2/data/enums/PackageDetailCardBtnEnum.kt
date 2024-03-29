package com.patronusstudio.sisecevirmece2.data.enums

import androidx.compose.ui.graphics.Color
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor

enum class PackageDetailCardBtnEnum {
    NEED_DOWNLOAD {
        override fun butonColor(): Color = AppColor.GreenMalachite

        override fun butonText(): Int = R.string.download
    },
    NEED_UPDATE {
        override fun butonColor(): Color = AppColor.Mustard

        override fun butonText(): Int = R.string.update
    },
    REMOVABLE {
        override fun butonColor(): Color = AppColor.SunsetOrange

        override fun butonText(): Int = R.string.remove
    }, ;

    abstract fun butonColor(): Color
    abstract fun butonText(): Int
}
