package com.chooloo.www.chooloolib.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.chooloolib.domain.repository.color.ColorRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.theme.ThemeRepository
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject lateinit var themeRepository: ThemeRepository
    @Inject lateinit var colorRepository: ColorRepository
    @Inject lateinit var preferenceRepository: PreferenceRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeRepository.applyThemeMode(preferenceRepository.themeMode.value)

        onSetup()
    }

    override fun finish() {
        finishAndRemoveTask()
    }

    abstract fun onSetup()
}