package com.chooloo.www.chooloolib.ui.contact

import android.view.View
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment @Inject constructor() : BaseFragment<ContactViewState>() {
    override val viewState:ContactViewState by viewModels()

    override val contentView: View
        get() = TODO("Not yet implemented")


    override fun onSetup() {
    }
}