package com.chooloo.www.chooloolib.ui.prompt

import com.chooloo.www.chooloolib.ui.base.BaseContract

class PromptContract : BaseContract {
    interface View : BaseContract.View {
        var title: String?
        var subtitle: String?
    }

    interface Controller : BaseContract.Controller<View> {
        fun onNoClick()
        fun onYesClick()
        fun setOnClickListener(onClickListener: (result: Boolean) -> Unit)
    }
}