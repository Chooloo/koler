package com.chooloo.www.chooloolib.interactor.theme

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.ThemeMode.SYSTEM
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemesInteractorImpl @Inject constructor(
    private val preferences: PreferencesInteractor,
    @ApplicationContext private val context: Context
) : BaseObservable<ThemesInteractor.Listener>(), ThemesInteractor {
    override fun applyTheme() {
        if (preferences.themeMode == SYSTEM) {
            DynamicColors.applyToActivitiesIfAvailable(context as Application)
        }
        AppCompatDelegate.setDefaultNightMode(preferences.themeMode.mode)
    }
}