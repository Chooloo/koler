package com.chooloo.www.chooloolib.ui.contact

import android.view.View
import com.chooloo.www.chooloolib.ui.base.BaseContract
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment @Inject constructor() : BaseFragment(), ContactContract.View {
    override lateinit var controller:ContactContract.Controller

    override val contentView: View
        get() = TODO("Not yet implemented")


    override fun onSetup() {
        controller = controllerFactory.getContactController(this)
    }
}