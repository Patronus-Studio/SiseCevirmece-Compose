package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece2.R

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
    },
    TAS1{
        override fun getImageId(): Int = R.drawable.tas1

        override fun getBottleName(context: Context): String =
            "Taş 1 Arka Plan"
    },
    TAS2{
        override fun getImageId(): Int = R.drawable.tas2

        override fun getBottleName(context: Context): String =
            "Taş 2 Arka Plan"
    },
    TAS3{
        override fun getImageId(): Int = R.drawable.tas3

        override fun getBottleName(context: Context): String =
            "Taş 3 Arka Plan"
    },
    TAS4{
        override fun getImageId(): Int = R.drawable.tas4

        override fun getBottleName(context: Context): String =
            "Taş 4 Arka Plan"
    },
    TAS5{
        override fun getImageId(): Int = R.drawable.tas5

        override fun getBottleName(context: Context): String =
            "Taş 5 Arka Plan"
    },
    MERMER_HARD{
        override fun getImageId(): Int = R.drawable.mermerhard

        override fun getBottleName(context: Context): String =
            "Koyu Mermer Arka Plan"
    },
    MERMER_LIGHT{
        override fun getImageId(): Int = R.drawable.mermerlight

        override fun getBottleName(context: Context): String =
            "Açık Mermer Arka Olan"
    },
    TUGLA{
        override fun getImageId(): Int = R.drawable.tugla

        override fun getBottleName(context: Context): String =
            "Tuğla Arka Plan"
    };

    abstract fun getImageId(): Int
    abstract fun getBottleName(context: Context): String
}