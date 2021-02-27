package com.chooloo.www.koler.ui.about

import com.chooloo.www.koler.ui.base.MvpPresenter

interface AboutMvpPresenter<V : AboutMvpView> : MvpPresenter<V> {
    fun onOpenSourceClick()
    fun onSendEmailClick()
    fun onReportBugClick()
    fun onRateAppClick()
    fun onDonateClick()
}