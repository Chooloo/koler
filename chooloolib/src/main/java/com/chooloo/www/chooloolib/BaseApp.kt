package com.chooloo.www.chooloolib

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        PreferenceManager.setDefaultValues(this, R.xml.preferences_chooloo, false)
    }
}