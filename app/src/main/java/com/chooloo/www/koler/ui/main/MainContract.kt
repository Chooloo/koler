package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var selectedPage: Int
        var dialpadNumber: String?

        fun openDialpad()
        fun checkIntent()
        fun openSettings()
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onMenuClick()
        fun onDialpadFabClick()
        fun onViewIntent(intent: Intent)
    }
}