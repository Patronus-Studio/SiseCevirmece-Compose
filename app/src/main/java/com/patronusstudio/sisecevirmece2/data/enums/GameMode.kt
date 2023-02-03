package com.patronusstudio.sisecevirmece2.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece2.R

enum class GameMode {
    NORMAL_MODE {
        override fun getTitle(context: Context): String =
            context.getString(R.string.play_normal_title)

        override fun getContent(context: Context): String =
            context.getString(R.string.play_normal_content)
    },
    SPECIAL_MODE {
        override fun getTitle(context: Context): String =
            context.getString(R.string.play_special_title)

        override fun getContent(context: Context): String =
            context.getString(R.string.play_special_content)
    };

    abstract fun getTitle(context: Context): String
    abstract fun getContent(context: Context): String
}