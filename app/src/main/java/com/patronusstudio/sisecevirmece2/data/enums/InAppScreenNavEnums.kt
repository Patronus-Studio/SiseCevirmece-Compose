package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece2.R

enum class InAppScreenNavEnums {
    INIT {
        override fun getText(context: Context): String = context.getString(R.string.home_screen)
    },
    PLAY_GAME{
        override fun getText(context: Context): String = context.getString(R.string.play_bigger)
    },
    LOGOUT {
        override fun getText(context: Context): String = context.getString(R.string.log_out)
    },
    STORES{
        override fun getText(context: Context): String = context.getString(R.string.store)
    },
    ADD_CATEGORIES{
        override fun getText(context: Context): String = context.getString(R.string.add_category)
    },
    PROFILE{
        override fun getText(context: Context): String = context.getString(R.string.my_profile)
    };

    abstract fun getText(context: Context):String
}