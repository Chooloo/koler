package com.chooloo.www.chooloolib.ui.prompt

import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

class PromptController @Inject constructor(
    view: PromptContract.View
) :
    BaseController<PromptContract.View>(view),
    PromptContract.Controller {

    private var onClickListener: (result: Boolean) -> Unit = {}


    override fun onNoClick() {
        onClickListener.invoke(false)
        view.finish()
    }

    override fun onYesClick() {
        onClickListener.invoke(true)
        view.finish()
    }


    override fun setOnClickListener(onClickListener: (result: Boolean) -> Unit) {
        this.onClickListener = onClickListener
    }
}