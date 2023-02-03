package com.patronusstudio.sisecevirmece2.data.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String {
    val stamp = Timestamp(System.currentTimeMillis()) // from java.sql.timestamp
    val date = Date(stamp.time)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}