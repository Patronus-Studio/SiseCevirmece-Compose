package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

data class LevelResponseModel(
    val data: List<LevelModel>,
    val message: String,
    val status: HttpStatusEnum
)