package com.chooloo.www.kontacts.ui.main

import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.utils.disableKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onSetup() {
        disableKeyboard()
        mainViewModel.onViewIntent(intent)

        setChoolooContent {
            MainScreen(mainViewModel = mainViewModel)
        }
    }
}
