package com.pagingsampleproject.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class AppExecuter constructor(var diskIO: Executor, var networkIO: Executor, var mainThread: Executor) {

    companion object {

        var INSTANCE: AppExecuter? = null

        fun getAppExecutor(): AppExecuter {
            if (INSTANCE == null) {
                INSTANCE = AppExecuter()
            }
            return INSTANCE!!
        }
    }

    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecuter()
    )

    class MainThreadExecuter : Executor {
        var handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            handler.post(command)
        }

    }
}