package com.chooloo.www.kontacts.ui.main

import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onSetup() {
        mainViewModel.onViewIntent(intent)

        setChoolooContent {
            MainScreen(mainViewModel = mainViewModel)
        }
    }
}
