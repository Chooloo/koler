package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.koler.AppConstants
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
        when (intent.action) {
            AppConstants.ACTION_SHOW_MISSED_CALLS -> mainViewModel.onMissedCallsOpened()

            Intent.ACTION_DIAL, Intent.ACTION_VIEW ->
                intent.data?.schemeSpecificPart?.let(mainViewModel::onDialNumber)
        }
    }
}
