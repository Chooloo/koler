package com.chooloo.www.chooloolib

import android.app.Application
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor
import javax.inject.Inject

abstract class ChoolooApp : Application() {
    @Inject lateinit var themes: ThemesInteractor
    @Inject lateinit var preferences: PreferencesInteractor

    override fun onCreate() {
        super.onCreate()
        preferences.setDefaultValues()
        themes.applyThemeMode(preferences.themeMode)
    }
}