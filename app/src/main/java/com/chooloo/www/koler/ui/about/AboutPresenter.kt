package com.chooloo.www.koler.ui.about

import com.chooloo.www.koler.ui.base.BasePresenter

class AboutPresenter<V : AboutMvpView> : BasePresenter<V>(), AboutMvpPresenter<V> {
    override fun onOpenSourceClick() {
        mvpView?.openSource()
    }

    override fun onSendEmailClick() {
        mvpView?.sendEmail()
    }

    override fun onReportBugClick() {
        mvpView?.reportBug()
    }

    override fun onRateAppClick() {
        mvpView?.rateApp()
    }

    override fun onDonateClick() {
        mvpView?.donate()
    }
}