package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.AvatarStatu

data class Avatar(
    val id:String,
    val url:String,
    var statu: AvatarStatu
)
