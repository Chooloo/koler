package com.chooloo.www.callmanager.ui.about

import com.chooloo.www.callmanager.ui.base.BasePresenter

class AboutPresenter<V : AboutMvpView?> : BasePresenter<V>(), AboutMvpPresenter<V> {
    override fun onChangelogClick() {}
    override fun onOpenSourceClick() {}
    override fun onSendEmailClick() {}
    override fun onReportBugClick() {}
    override fun onRateAppClick() {}
    override fun onDonateClick() {}
}