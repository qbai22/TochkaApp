package com.example.tochkaapp.data.repository.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor


/**
 * Created by Vladimir Kraev
 */
class MainThreadExecutor : Executor {

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }

}