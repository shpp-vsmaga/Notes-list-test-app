package com.noteslist.app.notes.gateway

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executor

class ExecuteOnCaller : Executor {
    private val threadLocalHandler = object : ThreadLocal<Handler>() {
        override fun initialValue(): Handler {
            val handlerThread = HandlerThread("io")
            handlerThread.start()
            val looper = handlerThread.looper
            Log.d("svcom", "lopper in initial - ${looper?.thread?.name}")
            return Handler(looper ?: Looper.getMainLooper())
        }
    }

    private val handler = threadLocalHandler.get()
    override fun execute(command: Runnable?) {
        command?.let {
            handler?.post(command)
        }
    }
}