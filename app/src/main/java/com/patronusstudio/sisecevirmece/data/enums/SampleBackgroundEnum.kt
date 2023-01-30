package com.patronusstudio.sisecevirmece.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece.R

enum class SampleBackgroundEnum {
    ORJINAL {
        override fun getImageId(): Int = R.drawable.background_original

        override fun getBottleName(context: Context): String =
            context.getString(R.string.original_background)
    },
    WOOD_LIGHT {
        override fun getImageId(): Int = R.drawable.background_wood_light

        override fun getBottleName(context: Context): String =
            context.getString(R.string.wood_light_background)
    },
    WOOD_NORMAL {
        override fun getImageId(): Int = R.drawable.background_wood_normal

        override fun getBottleName(context: Context): String =
            context.getString(R.string.wood_normal_background)
    },
    WOOD_TEXTURE {
        override fun getImageId(): Int = R.drawable.background_wood_texture

        override fun getBottleName(context: Context): String =
            context.getString(R.string.wood_texture_background)
    };

    abstract fun getImageId(): Int
    abstract fun getBottleName(context: Context): String
}