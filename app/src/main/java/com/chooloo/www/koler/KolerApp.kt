package com.chooloo.www.koler

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRootImpl
import com.chooloo.www.koler.di.component.ComponentRootImpl
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.notification.CallNotification

open class KolerApp : Application() {

    val componentRoot by lazy {
        ComponentRootImpl(this)
    }

    override fun onCreate() {
        super.onCreate()
        KolerApp.resources = resources
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CallNotification(this).createNotificationChannel()
        }
    }

    companion object {
        var resources: Resources? = null
    }
}