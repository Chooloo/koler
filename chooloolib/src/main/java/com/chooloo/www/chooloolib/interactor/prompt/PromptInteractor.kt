package com.chooloo.www.chooloolib.interactor.prompt

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment

interface PromptInteractor : BaseInteractor<PromptInteractor.Listener> {
    interface Listener

    fun showFragment(fragment: BaseFragment, tag: String? = null)
}