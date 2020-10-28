package com.noteslist.app.common.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object TextUtils {
    fun isEmailValid(email: String?): Boolean {
        val pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        val matcher = pattern.matcher(email ?: "")

        return matcher.matches()
    }

    fun getMaxLengthFromRegexp(regexp: String): Int {
        var maxLength = 0
        val filtered = regexp.substringAfterLast('{')
        val matcher: Matcher =
            Pattern.compile("\\d+")
                .matcher(filtered)
        while (matcher.find()) {
            maxLength = matcher.group().toInt()
        }
        return maxLength
    }
}