package com.chooloo.www.koler.ui.contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.FragmentContactBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call

class ContactFragment : BaseFragment(), ContactMvpView {
    companion object {
        const val TAG = "contact_fragment"
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }

    private val _presenter by lazy { ContactPresenter<ContactMvpView>() }
    private val _contact by lazy { _activity.lookupContact(argsSafely.getLong(ARG_CONTACT_ID)) }
    private val _binding by lazy { FragmentContactBinding.inflate(layoutInflater) }

    override var contactName: String?
        get() = _binding.contactTextName.text.toString()
        set(value) {
            _binding.contactTextName.text = value
        }

    override var contactNumber: String?
        get() = _binding.contactTextNumber.text.toString()
        set(value) {
            _binding.contactTextNumber.apply {
                text = value
                visibility = if (value != null) VISIBLE else GONE
            }
        }

    override var contactImage: Uri?
        get() = null
        set(value) {
            _binding.contactImage.apply {
                setImageURI(value)
                visibility = if (value != null) VISIBLE else GONE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return _binding.root
    }

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
            contactButtonCall.setOnClickListener { _presenter.onActionCall() }
            contactButtonSms.setOnClickListener { _presenter.onActionSms() }
            contactButtonEdit.setOnClickListener { _presenter.onActionEdit() }
            contactButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        animateLayout()
    }

    override fun callContact() {
        _contact.number?.let { _activity.call(it) }
    }

    override fun smsContact() {
        _activity.smsNumber(_contact.number)
    }

    override fun editContact() {
        _activity.editContact(_contact.id)
    }

    override fun openContact() {
        _activity.openContact(_contact.id)
    }

    override fun deleteContact() {
        _activity.deleteContact(_contact.id)
    }

    override fun animateLayout() {
        _binding.apply {
            animateViews(views = arrayOf(contactTextName, contactTextNumber), isShow = true)
        }
    }
}