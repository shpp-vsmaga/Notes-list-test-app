package com.noteslist.app.common.utils

fun <T> threadUnsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
