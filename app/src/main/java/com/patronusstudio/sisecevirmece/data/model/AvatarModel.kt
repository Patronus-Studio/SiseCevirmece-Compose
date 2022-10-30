package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.AvatarStatu

data class AvatarModel(
    val id: Int,
    val imageUrl: String,
    val starCount: Int,
    var buyedStatu: AvatarStatu
)