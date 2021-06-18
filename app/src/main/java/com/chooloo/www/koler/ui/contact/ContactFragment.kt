package com.chooloo.www.koler.ui.contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.ContactBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contactspreferences.ContactPreferencesFragment
import com.chooloo.www.koler.ui.phones.PhonesFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.permissions.runWithPrompt

class ContactFragment : BaseFragment(), ContactContract.View {
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _binding by lazy { ContactBinding.inflate(layoutInflater) }
    private val _contact by lazy { baseActivity.lookupContactId(_contactId) }
    private val _firstPhone by lazy { _contact.phoneAccounts.getOrNull(0) }
    private val _presenter by lazy { ContactPresenter<ContactContract.View>(this) }
    private val _phonesFragment by lazy { PhonesFragment.newInstance(_contactId, false) }


    override var contactName: String?
        get() = _binding.contactTextName.text.toString()
        set(value) {
            _binding.contactTextName.text = value
        }

    override var contactImage: Uri?
        get() = null
        set(value) {
            _binding.contactImage.apply {
                setImageURI(value)
                visibility = if (value != null) VISIBLE else GONE
            }
        }

    override var isStarIconVisible: Boolean
        get() = _binding.contactButtonFav.visibility == VISIBLE
        set(value) {
            _binding.contactButtonFav.visibility = if (value) VISIBLE else GONE
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _presenter.onLoadContact(_contact)

        _binding.apply {
            contactButtonSms.setOnClickListener { _presenter.onActionSms() }
            contactButtonCall.setOnClickListener { _presenter.onActionCall() }
            contactButtonEdit.setOnClickListener { _presenter.onActionEdit() }
            contactButtonMenu.setOnClickListener { _presenter.onActionMenu() }
            contactButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        childFragmentManager
            .beginTransaction()
            .add(_binding.contactPhonesFragmentContainer.id, _phonesFragment)
            .commitNow()
    }


    //region contact view

    override fun showMenu() {
        BottomFragment(ContactPreferencesFragment.newInstance(_contact.id)).show(
            childFragmentManager,
            null
        )
    }

    override fun smsContact() {
        _firstPhone?.let { baseActivity.smsNumber(it.number) }
    }

    override fun callContact() {
        _firstPhone?.let { baseActivity.call(it.number) }
    }

    override fun editContact() {
        baseActivity.editContact(_contact.id)
    }

    override fun openContact() {
        baseActivity.openContact(_contact.id)
    }

    override fun deleteContact() {
        baseActivity.runWithPrompt(R.string.warning_delete_contact) {
            baseActivity.deleteContact(_contact.id)
            finish()
        }
    }

    override fun setFavorite(isFavorite: Boolean) {
        baseActivity.setContactFavorite(_contact.id, isFavorite)
    }

    //endregion


    companion object {
        const val TAG = "contact_fragment"
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}