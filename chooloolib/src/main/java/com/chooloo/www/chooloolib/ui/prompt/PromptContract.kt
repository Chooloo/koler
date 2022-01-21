package com.chooloo.www.chooloolib.ui.prompt

import com.chooloo.www.chooloolib.ui.base.BaseContract

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