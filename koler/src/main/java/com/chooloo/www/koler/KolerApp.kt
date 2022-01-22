package com.chooloo.www.koler

import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.data.call.CallNotification

open class KolerApp : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.preferences_chooloo, false)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CallNotification(this).createNotificationChannel()
        }
    }
}