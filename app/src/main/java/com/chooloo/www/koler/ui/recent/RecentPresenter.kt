package com.chooloo.www.koler.ui.recent

import android.Manifest
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.getElapsedTimeString
import java.util.*

class RecentPresenter<V : RecentContract.View>(view: V) :
    BasePresenter<V>(view),
    RecentContract.Presenter<V> {

    private val _recent by lazy { boundComponent.recentsInteractor.queryRecent(view.recentId) }

    override fun onStart() {
        super.onStart()
        if (_recent == null) return
        val recentCaptions = arrayListOf(_recent!!.relativeTime)
        if (_recent!!.duration > 0) {
            recentCaptions.add(getElapsedTimeString(_recent!!.duration))
        }
        boundComponent.permissionInteractor.runWithDefaultDialer {
            if (boundComponent.numbersInteractor.isNumberBlocked(_recent!!.number)) {
                recentCaptions.add(
                    boundComponent.stringInteractor.getString(R.string.error_blocked)
                        .toUpperCase(Locale.ROOT)
                )
            }
        }
        view.recentCaption = recentCaptions.joinToString(", ")
        view.recentImage = boundComponent.drawableInteractor.getDrawable(
            RecentsContentResolver.getCallTypeImage(_recent!!.type)
        )
        view.recentName = _recent!!.cachedName ?: _recent!!.number

        boundComponent.phoneAccountsInteractor.lookupAccount(_recent!!.number) {
            view.isContactVisible = it.name != null
            view.isAddContactVisible = it.name == null
        }
    }

    override fun onActionMenu() {
        _recent?.let { view.showRecentMenu(it.number) }
    }

    override fun onActionSms() {
        _recent?.let { boundComponent.contactsInteractor.openSmsView(it.number) }
    }

    override fun onActionCall() {
        _recent?.let { boundComponent.navigationInteractor.call(it.number) }
    }

    override fun onActionDelete() {
        _recent?.let {
            boundComponent.permissionInteractor.runWithPermissions(
                arrayOf(Manifest.permission.WRITE_CALL_LOG), {
                    boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_recent) {
                        boundComponent.recentsInteractor.deleteRecent(it.id)
                    }
                },
                null, null, null
            )
        }
    }

    override fun onActionAddContact() {
        _recent?.let { boundComponent.contactsInteractor.openAddContactView(it.number) }
    }

    override fun onActionOpenContact() {
        _recent?.let {
            boundComponent.phoneAccountsInteractor.lookupAccount(it.number) {
                it.contactId?.let { view.openContactView(it) }
            }
        }
    }

    override fun onActionShowHistory() {
        _recent?.let { view.openHistoryView(it.number) }
    }
}