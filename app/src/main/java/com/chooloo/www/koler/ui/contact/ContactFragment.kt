package com.chooloo.www.koler.ui.contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.ContactBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contactspreferences.ContactPreferencesFragment
import com.chooloo.www.koler.ui.phones.PhonesFragment

class ContactFragment : BaseFragment(), ContactContract.View {
    override val contentView by lazy { _binding.root }
    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }

    private lateinit var _presenter: ContactController<ContactFragment>
    private val _binding by lazy { ContactBinding.inflate(layoutInflater) }
    private val _phonesFragment by lazy { PhonesFragment.newInstance(contactId) }

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
        get() = _binding.contactImageStar.visibility == VISIBLE
        set(value) {
            _binding.contactImageStar.visibility = if (value) VISIBLE else GONE
        }


    override fun onSetup() {
        _presenter = ContactController(this)

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

    override fun showMenu() {
//        BottomFragment(ContactPreferencesFragment.newInstance(contactId)).show(
//            childFragmentManager,
//            null
//        )
    }

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