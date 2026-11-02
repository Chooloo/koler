package com.chooloo.www.chooloolib.ui.common

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.ui.theme.ChoolooTheme
import javax.inject.Inject

abstract class BaseActivity : ComponentActivity() {
    @Inject lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val transparentBars = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = transparentBars, navigationBarStyle = transparentBars)
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)
    }

    protected fun setChoolooContent(content: @Composable () -> Unit) {
        setContent {
            val settings by preferenceRepository.settings.collectAsStateWithLifecycle(initialValue = null)
            settings?.let { ChoolooTheme(themeMode = it.themeMode, content = content) }
        }
    }
}
