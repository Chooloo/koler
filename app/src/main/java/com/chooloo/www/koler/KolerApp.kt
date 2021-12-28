package com.chooloo.www.koler

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.chooloo.www.koler.data.call.CallNotification
import com.chooloo.www.koler.di.contextcomponent.ContextComponentImpl

open class KolerApp : Application() {
    val component by lazy { ContextComponentImpl(this) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CallNotification(this).createNotificationChannel()
        }
    }
}