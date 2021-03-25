package com.chooloo.www.koler

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

open class App : Application() {

    companion object {
        var resources: Resources? = null
    }

    override fun onCreate() {
        super.onCreate()
        App.resources = resources
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
    }
}