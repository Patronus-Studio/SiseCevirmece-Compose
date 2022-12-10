package com.patronusstudio.sisecevirmece.data.enums

import androidx.compose.ui.graphics.Color

enum class PackageTitlesButonEnum {
    CREATED {
        override fun getTitles() = "Oluşturduklarım"

        override fun activeBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))

        override fun activeButonText(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveButtonText(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))
    },
    DOWNLOADED {
        override fun getTitles() = "İndirdiklerim"

        override fun activeBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))

        override fun activeButonText(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveBackgrounColor(): Color =
            Color(android.graphics.Color.parseColor("#ffffff"))

        override fun passiveButtonText(): Color =
            Color(android.graphics.Color.parseColor("#C689C6"))
    };

    abstract fun getTitles(): String
    abstract fun activeBackgrounColor(): Color
    abstract fun passiveBackgrounColor(): Color
    abstract fun activeButonText(): Color
    abstract fun passiveButtonText(): Color
}
