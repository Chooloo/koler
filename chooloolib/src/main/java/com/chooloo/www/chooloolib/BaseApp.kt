package com.chooloo.www.chooloolib

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import javax.inject.Inject

abstract class BaseApp : Application() {
    @Inject lateinit var preferences: PreferencesInteractor

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(preferences.themeMode.mode)
        PreferenceManager.setDefaultValues(this, R.xml.preferences_chooloo, false)
    }
}