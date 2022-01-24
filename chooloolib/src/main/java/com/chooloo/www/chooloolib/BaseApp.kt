package com.chooloo.www.chooloolib

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.di.contextcomponent.ContextComponentImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApp : Application() {
    open val component by lazy { ContextComponentImpl(this) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        PreferenceManager.setDefaultValues(this, R.xml.preferences_chooloo, false)
    }
}