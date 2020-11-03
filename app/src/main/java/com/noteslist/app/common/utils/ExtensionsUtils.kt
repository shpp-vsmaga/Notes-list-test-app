package com.noteslist.app.common.utils

import com.google.firebase.Timestamp
import org.joda.time.DateTime

fun <T> threadUnsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Timestamp.toJodaDateTime(): DateTime =
    DateTime(this.seconds * 1000)