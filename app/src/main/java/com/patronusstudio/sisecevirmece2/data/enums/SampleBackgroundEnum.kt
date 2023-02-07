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
            context.getString(R.string.stone_1_background)
    },
    TAS2{
        override fun getImageId(): Int = R.drawable.tas2

        override fun getBottleName(context: Context): String =
            context.getString(R.string.stone_2_background)
    },
    TAS3{
        override fun getImageId(): Int = R.drawable.tas3

        override fun getBottleName(context: Context): String =
            context.getString(R.string.stone_3_background)
    },
    TAS4{
        override fun getImageId(): Int = R.drawable.tas4

        override fun getBottleName(context: Context): String =
            context.getString(R.string.stone_4_background)
    },
    TAS5{
        override fun getImageId(): Int = R.drawable.tas5

        override fun getBottleName(context: Context): String =
            context.getString(R.string.stone_5_background)
    },
    MERMER_HARD{
        override fun getImageId(): Int = R.drawable.mermerhard

        override fun getBottleName(context: Context): String =
            context.getString(R.string.marble_hard_background)
    },
    MERMER_LIGHT{
        override fun getImageId(): Int = R.drawable.mermerlight

        override fun getBottleName(context: Context): String =
            context.getString(R.string.marble_light_background)
    },
    TUGLA{
        override fun getImageId(): Int = R.drawable.tugla

        override fun getBottleName(context: Context): String =
            context.getString(R.string.brink_background)
    };

    abstract fun getImageId(): Int
    abstract fun getBottleName(context: Context): String
}