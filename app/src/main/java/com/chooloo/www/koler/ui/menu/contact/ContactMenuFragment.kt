package com.chooloo.www.koler.ui.menu.contact

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.menu.MenuFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.permissions.runWithPrompt

class ContactMenuFragment : MenuFragment(), ContactMenuContract.View {
    override val menuRes = R.menu.contact_extra
    private val _presenter by lazy { ContactMenuPresenter<ContactMenuContract.View>() }
    private val _contact by lazy { _activity.lookupContactId(argsSafely.getLong(ARG_CONTACT_ID)) }

    companion object {
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactMenuFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onAttachMenu(menu: Menu) {
        menu.removeItem(if (_activity.isContactBlocked(_contact)) R.id.contact_extra_block else R.id.contact_extra_unblock)
        menu.removeItem(if (_contact.starred) R.id.contact_extra_like else R.id.contact_extra_unlike)
    }

    override fun onItemClick(item: MenuItem) {
        _presenter.onMenuItemClick(item)
    }

    override fun blockContact() {
        _activity.runWithPrompt(R.string.warning_block_contact) {
            _contact.phoneAccounts.forEach { _activity.blockNumber(it.number) }
            showMessage(R.string.contact_blocked)
        }
    }

    override fun unblockContact() {
        _contact.phoneAccounts.forEach { _activity.unblockNumber(it.number) }
        showMessage(R.string.contact_unblocked)
    }

    override fun setContactFavorite() {
        _activity.setContactFavorite(_contact.id, true)
    }

    override fun unsetContactFavorite() {
        _activity.setContactFavorite(_contact.id, false)
    }
}