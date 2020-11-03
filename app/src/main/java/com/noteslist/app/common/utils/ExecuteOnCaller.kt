package com.noteslist.app.common.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.Executor

class ExecuteOnCaller : Executor {
    private val threadLocalHandler = object : ThreadLocal<Handler>() {
        override fun initialValue(): Handler {
            val handlerThread = HandlerThread(FIRESTORE_HANDLER_TRADE_NAME)
            handlerThread.start()
            val looper = handlerThread.looper
            return Handler(looper ?: Looper.getMainLooper())
        }
    }

    private val handler = threadLocalHandler.get()
    override fun execute(command: Runnable?) {
        command?.let {
            handler?.post(command)
        }
    }

    companion object {
        const val FIRESTORE_HANDLER_TRADE_NAME = "firestore_io"
    }
}