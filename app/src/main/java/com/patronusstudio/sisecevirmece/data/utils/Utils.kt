package com.patronusstudio.sisecevirmece.data.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

fun checkEmailCorrect(email: String): Boolean {
    if (email.isEmpty()) {
        return false
    }
    val droppedLastBackspace = email.dropLastWhile {
        it == ' '
    }
    val pttn = "^\\D.+@.+\\.[a-z]+"
    val p = Pattern.compile(pttn)
    val m: Matcher = p.matcher(droppedLastBackspace)
    return m.matches()
}