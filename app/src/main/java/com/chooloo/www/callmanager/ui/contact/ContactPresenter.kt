package com.chooloo.www.callmanager.ui.contact

import com.chooloo.www.callmanager.ui.base.BasePresenter

class ContactPresenter<V : ContactMvpView> : BasePresenter<V>(), ContactMvpPresenter<V> {

    override fun onActionCall() {
        mvpView?.call()
    }

    override fun onActionSms() {
        mvpView?.sms()
    }

    override fun onActionEdit() {
        mvpView?.edit()
    }

    override fun onActionInfo() {
        mvpView?.open()
    }

    override fun onActionDelete() {
        mvpView?.delete()
    }
}