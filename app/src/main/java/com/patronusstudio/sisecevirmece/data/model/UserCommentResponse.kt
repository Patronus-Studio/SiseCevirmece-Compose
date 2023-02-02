package com.patronusstudio.sisecevirmece.data.model

data class UserCommentRequest(
    val appVersion: String,
    val comment: String,
    val deviceModel: String,
    val deviceType: String,
    val sendDate: String,
    val starCount: Float,
    val username: String
)