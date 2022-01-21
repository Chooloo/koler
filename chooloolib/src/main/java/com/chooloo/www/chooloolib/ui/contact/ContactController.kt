package com.chooloo.www.chooloolib.ui.contact

import com.chooloo.www.chooloolib.ui.base.BaseController

class ContactController<V : ContactContract.View>(view: V) :
    BaseController<V>(view),
    ContactContract.Controller<V> {
        
}