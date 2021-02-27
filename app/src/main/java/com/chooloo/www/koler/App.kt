package com.chooloo.www.koler

import android.app.Application
import android.content.res.Resources

open class App : Application() {

    companion object {
        var resources: Resources? = null
    }

    override fun onCreate() {
        super.onCreate()
        App.resources = resources
    }
}