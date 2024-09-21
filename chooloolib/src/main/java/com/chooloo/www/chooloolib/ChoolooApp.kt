package com.chooloo.www.chooloolib

import android.app.Application
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.theme.ThemeRepository
import javax.inject.Inject

abstract class ChoolooApp : Application() {
    @Inject lateinit var themeRepository: ThemeRepository
    @Inject lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()
        themeRepository.applyThemeMode(preferenceRepository.themeMode.value)
    }
}