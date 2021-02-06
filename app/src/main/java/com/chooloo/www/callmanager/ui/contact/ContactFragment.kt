package com.chooloo.www.callmanager.ui.contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chooloo.www.callmanager.databinding.FragmentContactBinding
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.service.CallManager.call
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.util.*

class ContactFragment : BaseFragment(), ContactMvpView {

    private lateinit var _presenter: ContactPresenter<ContactMvpView>
    private lateinit var _contact: Contact
    private lateinit var _binding: FragmentContactBinding

    companion object {
        const val CONTACT_ARG = "contact"

        @JvmStatic
        fun newInstance(contact: Contact): ContactFragment {
            return ContactFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CONTACT_ARG, contact)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactBinding.inflate(inflater)
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = ContactPresenter()
        _presenter.attach(this)

        // contact details
        _contact = argsSafely.getSerializable(CONTACT_ARG) as Contact

        _binding.apply {
            contactTextName.text = _contact.name
            contactTextNumber.text = _contact.number
            contactFavoriteIcon.visibility = if (_contact.starred) View.VISIBLE else View.GONE
            if (_contact.photoUri != null) {
                contactImage.setImageURI(Uri.parse(_contact.photoUri))
            }

            // click listeners
            contactButtonCall.setOnClickListener { _presenter.onActionCall() }
            contactButtonSms.setOnClickListener { _presenter.onActionSms() }
            contactButtonEdit.setOnClickListener { _presenter.onActionEdit() }
            contactButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        animateLayout()
    }

    override fun call() {
        call(_activity, _contact.number)
    }

    override fun sms() {
        smsContact(_activity, _contact)
    }

    override fun edit() {
        editContact(_activity, _contact)
    }

    override fun open() {
        openContact(_activity, _contact)
    }

    override fun delete() {
        deleteContact(_activity, _contact)
    }

    override fun animateLayout() {
        _binding.apply {
            animateViews(arrayOf(
                    contactImage,
                    contactTextName,
                    contactActionsLayout
            ), 130, true)
        }
    }
}