package com.patronusstudio.sisecevirmece.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece.R

enum class SampleBottleEnum {
    ORJINAL{
        override fun getImageId(): Int = R.drawable.bottle_sample

        override fun getBottleName(context: Context): String = context.getString(R.string.orginal_bottle)
    },
    BEER {
        override fun getImageId(): Int = R.drawable.beer

        override fun getBottleName(context: Context): String = context.getString(R.string.beer_bottle)
    },
    CHAMPAGNE{
        override fun getImageId(): Int = R.drawable.champagne

        override fun getBottleName(context: Context): String = context.getString(R.string.champagne_bottle)
    },
    COLA{
        override fun getImageId(): Int = R.drawable.cola

        override fun getBottleName(context: Context): String = context.getString(R.string.cola_bottle)
    },
    TEA{
        override fun getImageId(): Int = R.drawable.tea

        override fun getBottleName(context: Context): String = context.getString(R.string.tea_kettle)
    },
    WHISKY{
        override fun getImageId(): Int = R.drawable.whisky

        override fun getBottleName(context: Context): String = context.getString(R.string.whisky_bottle)
    },
    WINE{
        override fun getImageId(): Int = R.drawable.wine

        override fun getBottleName(context: Context): String = context.getString(R.string.wine_bottle)
    },
    WINE2{
        override fun getImageId(): Int = R.drawable.wine2

        override fun getBottleName(context: Context): String = context.getString(R.string.wine_bottle)
    };

    abstract fun getImageId():Int
    abstract fun getBottleName(context: Context):String
}