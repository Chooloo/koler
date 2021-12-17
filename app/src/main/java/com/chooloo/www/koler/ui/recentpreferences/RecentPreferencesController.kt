package com.chooloo.www.koler.ui.recentpreferences

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseController

class RecentPreferencesController<V : RecentPreferencesContract.View>(view: V) :
    BaseController<V>(view),
    RecentPreferencesContract.Controller<V> {

    override fun onStart() {
        super.onStart()
        component.permissions.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            view.isBlockNumberVisible =
                view.number?.let { !component.blocked.isNumberBlocked(it) } ?: false
            view.isUnblockNumberVisible =
                view.number?.let { component.blocked.isNumberBlocked(it) } ?: false
        }
    }

    override fun onBlockNumberClick() {
        view.number?.let { number ->
            component.permissions.runWithPrompt(R.string.warning_block_number) {
                if(it) {
                    component.blocked.blockNumber(number)
                    view.showMessage(R.string.number_blocked)
                }
            }
        }
    }

    override fun onUnblockNumberClick() {
        view.number?.let {
            component.blocked.unblockNumber(it)
            view.showMessage(R.string.number_unblocked)
        }
    }
}