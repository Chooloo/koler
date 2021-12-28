package com.chooloo.www.koler.ui.prompt

import com.chooloo.www.koler.ui.base.BaseContract

class PromptContract : BaseContract {
    interface View : BaseContract.View {
        var title: String?
        var subtitle: String?
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onNoClick()
        fun onYesClick()
    }
}