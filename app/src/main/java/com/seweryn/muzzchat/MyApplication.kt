package com.seweryn.muzzchat

import android.app.Application
import com.seweryn.chat.di.chatModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(chatModule)
        }
    }
}
