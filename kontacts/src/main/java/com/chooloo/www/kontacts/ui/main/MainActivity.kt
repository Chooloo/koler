package com.chooloo.www.kontacts.ui.main

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
            MainScreen(mainViewModel = mainViewModel)
        }
    }
}
