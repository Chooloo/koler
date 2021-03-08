package com.chooloo.www.koler.ui.about

interface AboutMvpPresenter<V : AboutMvpView> : MvpPresenter<V> {
    fun onOpenSourceClick()
    fun onSendEmailClick()
    fun onReportBugClick()
    fun onRateAppClick()
    fun onDonateClick()
}