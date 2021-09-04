package com.chooloo.www.koler.ui.recent

import android.Manifest
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.getElapsedTimeString
import java.util.*

class RecentPresenter<V : RecentContract.View>(view: V) :
    BasePresenter<V>(view),
    RecentContract.Presenter<V> {

    private var _recent: Recent? = null

    override fun onStart() {
        super.onStart()
        boundComponent.recentsInteractor.getRecent(view.recentId) { recent ->
            if (recent == null) return@getRecent
            _recent = recent
            val recentCaptions = arrayListOf(recent.relativeTime)
            if (recent.duration > 0) {
                recentCaptions.add(getElapsedTimeString(recent.duration))
            }
            boundComponent.permissionInteractor.runWithDefaultDialer {
                if (boundComponent.numbersInteractor.isNumberBlocked(recent.number)) {
                    recentCaptions.add(
                        boundComponent.stringInteractor.getString(R.string.error_blocked)
                            .toUpperCase(Locale.ROOT)
                    )
                }
            }
            view.recentCaption = recentCaptions.joinToString(", ")
            view.recentImage = boundComponent.drawableInteractor.getDrawable(
                RecentsContentResolver.getCallTypeImage(recent.type)
            )
            view.recentName = recent.cachedName ?: recent.number

            boundComponent.phoneAccountsInteractor.lookupAccount(recent.number) {
                view.isContactVisible = it.name != null
                view.isAddContactVisible = it.name == null
            }
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
                arrayOf(Manifest.permission.WRITE_CALL_LOG),
                { boundComponent.recentsInteractor.deleteRecent(it.id) },
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