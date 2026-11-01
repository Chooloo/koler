package com.chooloo.www.kontacts.ui.activity

import androidx.activity.viewModels
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import com.chooloo.www.chooloolib.utils.disableKeyboard
import com.chooloo.www.kontacts.ui.view.KontactsView
import com.chooloo.www.kontacts.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onSetup() {
        disableKeyboard()
        mainViewModel.onViewIntent(intent)

        setChoolooContent {
            KontactsView(mainViewModel = mainViewModel)
        }
    }
}
