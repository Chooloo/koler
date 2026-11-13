package com.chooloo.www.kontacts.ui.main

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
        if (savedInstanceState == null) handleIntent(intent)
        setChoolooContent {
            MainScreen(viewModel = mainViewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) intent.data?.let { mainViewModel.onContactLinkOpened(it.toString()) }
    }
}
