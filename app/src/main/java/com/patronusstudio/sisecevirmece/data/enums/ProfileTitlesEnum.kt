package com.patronusstudio.sisecevirmece.data.enums

import android.content.Context
import com.patronusstudio.sisecevirmece.R

enum class ProfileTitlesEnum {
    BOTTLES {
        override fun getTitles(context: Context): String = context.getString(R.string.bottles)
        override fun getActiveBtnColor(): String = "#8BC34A"
        override fun getActiveTextColor(): String = "#F5F5F5"
        override fun getPassiveBtnColor(): String = "#F5F5F5"
        override fun getPassiveTextColor(): String = "#8BC34A"

    },PACKAGES{
        override fun getTitles(context: Context): String = context.getString(R.string.packages)
        override fun getActiveBtnColor(): String = "#8BC34A"
        override fun getActiveTextColor(): String = "#F5F5F5"
        override fun getPassiveBtnColor(): String = "#F5F5F5"
        override fun getPassiveTextColor(): String = "#8BC34A"
    },BACKGROUNDS{
        override fun getTitles(context: Context): String = context.getString(R.string.backgrounds)
        override fun getActiveBtnColor(): String = "#8BC34A"
        override fun getActiveTextColor(): String = "#F5F5F5"
        override fun getPassiveBtnColor(): String = "#F5F5F5"
        override fun getPassiveTextColor(): String = "#8BC34A"
    };
    
    abstract fun getTitles(context: Context):String
    abstract fun getActiveBtnColor():String
    abstract fun getActiveTextColor():String
    abstract fun getPassiveBtnColor():String
    abstract fun getPassiveTextColor():String

    companion object{
        fun getEnumWithIndex(index:Int):ProfileTitlesEnum{
            return when(index){
                0 -> BOTTLES
                1 -> PACKAGES
                2 -> BACKGROUNDS
                else ->BOTTLES
            }
        }
    }
}