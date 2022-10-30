package com.patronusstudio.sisecevirmece.data

import com.google.gson.annotations.SerializedName

enum class AvatarStatu(val buyedStatu:String) {
    @SerializedName("BUYED")
    BUYED("BUYED"),
    @SerializedName("NON_BUYED")
    NON_BUYED("NON_BUYED"),
}