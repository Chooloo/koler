package com.chooloo.www.chooloolib.ui.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.ui.theme.ChoolooTheme
import javax.inject.Inject

abstract class BaseActivity : ComponentActivity() {
    @Inject lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetup()
    }

    protected fun setChoolooContent(content: @Composable () -> Unit) {
        setContent {
            val themeMode by preferenceRepository.themeMode.collectAsStateWithLifecycle()
            ChoolooTheme(themeMode = themeMode, content = content)
        }
    }

    override fun finish() {
        finishAndRemoveTask()
    }

    abstract fun onSetup()
}
