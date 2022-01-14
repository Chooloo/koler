package com.chooloo.www.chooloolib

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chooloo.www.chooloolib.di.contextcomponent.ContextComponentImpl

open class BaseApp : Application() {
    val component by lazy { ContextComponentImpl(this) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}