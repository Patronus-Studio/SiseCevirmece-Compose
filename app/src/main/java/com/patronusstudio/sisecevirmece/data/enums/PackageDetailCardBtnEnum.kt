package com.patronusstudio.sisecevirmece.data.enums

import androidx.compose.ui.graphics.Color
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.GreenMalachite
import com.patronusstudio.sisecevirmece.ui.theme.Mustard
import com.patronusstudio.sisecevirmece.ui.theme.SunsetOrange

enum class PackageDetailCardBtnEnum {
    NEED_DOWNLOAD {
        override fun butonColor(): Color = GreenMalachite

        override fun butonText(): Int = R.string.download
    },
    NEED_UPDATE {
        override fun butonColor(): Color = Mustard

        override fun butonText(): Int = R.string.update
    },
    REMOVABLE {
        override fun butonColor(): Color = SunsetOrange

        override fun butonText(): Int = R.string.remove
    }, ;

    abstract fun butonColor(): Color
    abstract fun butonText(): Int
}
