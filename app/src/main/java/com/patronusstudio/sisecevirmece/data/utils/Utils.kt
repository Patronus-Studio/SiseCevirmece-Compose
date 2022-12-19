package com.patronusstudio.sisecevirmece.data.utils

import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.checkEmailCorrect(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    val droppedLastBackspace = this.dropLastWhile {
        it == ' '
    }
    val pttn = "^\\D.+@.+\\.[a-z]+"
    val p = Pattern.compile(pttn)
    val m: Matcher = p.matcher(droppedLastBackspace)
    return m.matches()
}

fun isConnected(): Boolean {
    val command = "ping -c 1 google.com"
    return Runtime.getRuntime().exec(command).waitFor() == 0
}