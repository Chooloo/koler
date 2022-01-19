package com.chooloo.www.chooloolib.ui.contact

import com.chooloo.www.chooloolib.ui.base.BaseContract

interface ContactContract : BaseContract {
    interface View : BaseContract.View {

    }

    interface Controller<V : View> : BaseContract.Controller<V> {

    }
}