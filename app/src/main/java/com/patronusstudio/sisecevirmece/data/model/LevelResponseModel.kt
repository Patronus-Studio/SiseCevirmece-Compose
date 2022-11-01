package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

data class LevelResponseModel(
    val data: List<LevelModel>,
    val message: String,
    val status: HttpStatusEnum
)