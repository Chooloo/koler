package com.chooloo.www.chooloolib.interactor.prompt

import androidx.fragment.app.FragmentManager
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.base.BottomFragment
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PromptInteractorImpl @Inject constructor(
    private val fragmentManager: FragmentManager
) : BaseInteractorImpl<PromptInteractor.Listener>(),
    PromptInteractor {

    override fun showFragment(fragment: BaseFragment, tag: String?) {
        BottomFragment(fragment).show(fragmentManager, tag)
    }
}