package com.chooloo.www.chooloolib.ui.contact

import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

class ContactController<V : ContactContract.View> @Inject constructor(view: V) :
    BaseController<V>(view),
    ContactContract.Controller<V> {
        
}