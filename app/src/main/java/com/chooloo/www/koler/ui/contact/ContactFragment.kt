package com.chooloo.www.koler.ui.contact

import ContactsUtils
import android.Manifest.permission.WRITE_CONTACTS
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.R
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.databinding.ContactBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contactspreferences.ContactPreferencesFragment
import com.chooloo.www.koler.ui.phones.PhonesFragment

class ContactFragment : BaseFragment(), ContactContract.View {
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _binding by lazy { ContactBinding.inflate(layoutInflater) }
    private val _presenter by lazy { ContactPresenter<ContactContract.View>(this) }
    private val _contact by lazy { boundComponent.contactsInteractor.getContact(_contactId) }
    private val _phonesFragment by lazy { PhonesFragment.newInstance(_contactId, false) }
    private val _firstPhone by lazy {
        boundComponent.phoneAccountsInteractor.getContactAccounts(_contactId).getOrNull(0)
    }

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
        _contact?.let { _presenter.onLoadContact(it) }

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
        _contact?.id?.let {
            BottomFragment(ContactPreferencesFragment.newInstance(it)).show(
                childFragmentManager,
                null
            )
        }
    }

    override fun smsContact() {
        _firstPhone?.number?.let { ContactsUtils.openSmsView(baseActivity, it) }
    }

    override fun callContact() {
        _firstPhone?.number?.let { CallManager.call(baseActivity, it) }
    }

    override fun editContact() {
        ContactsUtils.openEditContactView(baseActivity, _contactId)
    }

    override fun openContact() {
        ContactsUtils.openContactView(baseActivity, _contactId)
    }

    @SuppressLint("MissingPermission")
    override fun deleteContact() {
        boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_contact) {
            boundComponent.permissionInteractor.runWithPermissions(arrayOf(WRITE_CONTACTS), {
                boundComponent.contactsInteractor.deleteContact(_contactId)
            }, null, null, null)
            parentFragmentManager.popBackStack()
        }
    }

    @SuppressLint("MissingPermission")
    override fun setFavorite(isFavorite: Boolean) {
        boundComponent.permissionInteractor.runWithPermissions(arrayOf(WRITE_CONTACTS), {
            boundComponent.contactsInteractor.toggleContactFavorite(_contactId, isFavorite)
        }, null, null, null)
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