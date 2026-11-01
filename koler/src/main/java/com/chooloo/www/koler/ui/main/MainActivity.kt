package com.chooloo.www.koler.ui.main

import android.content.Intent
import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.utils.disableKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onSetup() {
        disableKeyboard()

        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            mainViewModel.onViewIntent(intent)
        }

        setChoolooContent {
            MainScreen(mainViewModel = mainViewModel)
        }
    }
}
