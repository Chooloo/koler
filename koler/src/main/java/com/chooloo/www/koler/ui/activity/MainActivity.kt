package com.chooloo.www.koler.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import com.chooloo.www.chooloolib.utils.disableKeyboard
import com.chooloo.www.koler.ui.view.KolerView
import com.chooloo.www.koler.ui.viewmodel.MainViewModel
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
            KolerView(mainViewModel = mainViewModel)
        }
    }
}
