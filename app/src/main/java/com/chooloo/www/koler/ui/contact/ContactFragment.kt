package com.chooloo.www.koler.ui.contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.FragmentContactBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.menu.contact.ContactMenuFragment
import com.chooloo.www.koler.ui.phones.PhonesFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.permissions.runWithPrompt

class ContactFragment : BaseFragment(), ContactContract.View {
    private val _contact by lazy { _activity.lookupContactId(_contactId) }
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _presenter by lazy { ContactPresenter<ContactContract.View>() }
    private val _binding by lazy { FragmentContactBinding.inflate(layoutInflater) }
    private val _phonesFragment by lazy { PhonesFragment.newInstance(_contactId, false) }
    private val _isBlocked by lazy { _contact.phoneAccounts.all { _activity.isNumberBlocked(it.number) } }

    companion object {
        const val TAG = "contact_fragment"
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
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

    override var isStarIconActivated: Boolean
        get() = _binding.contactButtonFav.isActivated
        set(value) {
            _binding.contactButtonFav.isActivated = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.apply {
            attach(this@ContactFragment)
            onLoadContact(_contact)
        }

        _binding.apply {
            contactButtonSms.setOnClickListener { _presenter.onActionSms() }
            contactButtonFav.setOnClickListener { _presenter.onActionFav() }
            contactButtonCall.setOnClickListener { _presenter.onActionCall() }
            contactButtonEdit.setOnClickListener { _presenter.onActionEdit() }
            contactButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            contactButtonMenu.setOnClickListener { _presenter.onActionMenu() }
        }

        childFragmentManager
            .beginTransaction()
            .add(_binding.contactPhonesFragmentContainer.id, _phonesFragment)
            .commitNow()
    }

    override fun showMenu() {
        BottomFragment(ContactMenuFragment.newInstance(_contact.id)).show(
            childFragmentManager,
            null
        )
    }

    override fun smsContact() {
        _contact.phoneAccounts.getOrNull(0)?.let { _activity.smsNumber(it.number) }
    }

    override fun callContact() {
        _contact.phoneAccounts.getOrNull(0)?.let { _activity.call(it.number) }
    }

    override fun editContact() {
        _activity.editContact(_contact.id)
    }

    override fun openContact() {
        _activity.openContact(_contact.id)
    }

    override fun deleteContact() {
        _activity.runWithPrompt(R.string.warning_delete_contact) {
            _activity.deleteContact(_contact.id)
            finish()
        }
    }

    override fun setFavorite(isFavorite: Boolean) {
        _activity.setFavorite(_contact.id, isFavorite)
    }
}