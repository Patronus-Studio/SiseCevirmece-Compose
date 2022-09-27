package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.LandingScreenEnum
import kotlin.random.Random

data class LandingModel(
    val text: String,
    val landingScreenEnum: LandingScreenEnum,
    val color: Long = Random.nextLong(0xFFFFFFFF),
)