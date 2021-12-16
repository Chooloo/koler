package com.chooloo.www.koler.ui.recent

import android.Manifest
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseController
import com.chooloo.www.koler.util.getElapsedTimeString
import java.util.*

class RecentController<V : RecentContract.View>(view: V) :
    BaseController<V>(view),
    RecentContract.Controller<V> {

    private val _recent by lazy { component.recents.queryRecent(view.recentId) }

    override fun onStart() {
        super.onStart()
        if (_recent == null) return
        val recentCaptions = arrayListOf(_recent!!.relativeTime)
        if (_recent!!.duration > 0) {
            recentCaptions.add(getElapsedTimeString(_recent!!.duration))
        }
        component.permissions.runWithDefaultDialer {
            if (component.blocked.isNumberBlocked(_recent!!.number)) {
                recentCaptions.add(
                    component.strings.getString(R.string.error_blocked).toUpperCase(Locale.ROOT)
                )
            }
        }
        view.recentCaption = recentCaptions.joinToString(", ")
        view.recentImage =
            component.drawables.getDrawable(component.recents.getCallTypeImage(_recent!!.type))
        view.recentName = _recent!!.cachedName ?: _recent!!.number

        component.phones.lookupAccount(_recent!!.number) {
            view.isContactVisible = it?.name != null
            view.isAddContactVisible = it?.name == null
        }
    }

    override fun onActionMenu() {
        _recent?.let { view.showRecentMenu(it.number) }
    }

    override fun onActionSms() {
        _recent?.let { component.navigations.sendSMS(it.number) }
    }

    override fun onActionCall() {
        _recent?.let { component.navigations.call(it.number) }
    }

    override fun onActionDelete() {
        _recent?.let {
            component.permissions.runWithPermissions(
                arrayOf(Manifest.permission.WRITE_CALL_LOG), {
                    component.permissions.runWithPrompt(R.string.warning_delete_recent) {
                        component.recents.deleteRecent(it.id)
                    }
                },
                null, null, null
            )
        }
    }

    override fun onActionAddContact() {
        _recent?.let { component.navigations.addContact(it.number) }
    }

    override fun onActionOpenContact() {
        _recent?.let {
            component.phones.lookupAccount(it.number) { account ->
                account?.contactId?.let(view::openContactView)
            }
        }
    }

    override fun onActionShowHistory() {
        _recent?.let { view.openHistoryView(it.number) }
    }
}