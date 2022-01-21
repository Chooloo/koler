package com.chooloo.www.chooloolib.ui.prompt

import com.chooloo.www.chooloolib.ui.base.BaseController

class PromptController<V : PromptContract.View>(view: V) :
    BaseController<V>(view),
    PromptContract.Controller<V> {

    private var onClickListener: (result: Boolean) -> Unit = {}


    override fun onNoClick() {
        onClickListener.invoke(false)
        view.finish()
    }

    override fun onYesClick() {
        onClickListener.invoke(true)
        view.finish()
    }


    fun setOnClickListener(onClickListener: (result: Boolean) -> Unit) {
        this.onClickListener = onClickListener
    }
}