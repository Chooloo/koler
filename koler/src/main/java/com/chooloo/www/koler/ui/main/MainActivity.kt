package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) mainViewModel.onViewIntent(intent)
        setChoolooContent {
            MainScreen(viewModel = mainViewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mainViewModel.onViewIntent(intent)
    }
}
