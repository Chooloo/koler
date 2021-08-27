package com.chooloo.www.koler.ui.recentpreferences

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPreferencesPresenter<V : RecentPreferencesContract.View>(view: V) :
    BasePresenter<V>(view),
    RecentPreferencesContract.Presenter<V> {

    override fun onStart() {
        super.onStart()
        boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            view.isBlockNumberVisible =
                view.number?.let { !boundComponent.numbersInteractor.isNumberBlocked(it) } ?: false
            view.isUnblockNumberVisible =
                view.number?.let { boundComponent.numbersInteractor.isNumberBlocked(it) } ?: false
        }
    }

    override fun onBlockNumberClick() {
        view.number?.let {
            boundComponent.permissionInteractor.runWithPrompt(R.string.warning_block_number) {
                boundComponent.numbersInteractor.blockNumber(it)
                view.showMessage(R.string.number_blocked)
            }
        }
    }

    override fun onUnblockNumberClick() {
        view.number?.let {
            boundComponent.numbersInteractor.unblockNumber(it)
            view.showMessage(R.string.number_unblocked)
        }
    }
}