package com.chooloo.www.koler.ui.recent

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.FragmentContactBinding
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call

class RecentFragment : BaseFragment(), RecentMvpView {

    private lateinit var _presenter: RecentPresenter<RecentMvpView>
    private lateinit var _recent: Recent
    private lateinit var _binding: FragmentContactBinding

    companion object {
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long): RecentFragment {
            return RecentFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CONTACT_ID, contactId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactBinding.inflate(inflater)
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = RecentPresenter()
        _presenter.attach(this)

        _recent = _activity.getRecent
        _contact = _activity.getContactbyId(argsSafely.getLong(ARG_CONTACT_ID))

        _binding.apply {
            contactTextName.text = _contact.name
            _contact.number?.let {
                contactTextNumber.text = it
                contactTextNumber.visibility = VISIBLE
            }
            _contact.photoUri?.let {
                contactImage.setImageURI(Uri.parse(it))
                contactImage.visibility = VISIBLE
            }
            contactButtonCall.setOnClickListener { _presenter.onActionCall() }
            contactButtonSms.setOnClickListener { _presenter.onActionSms() }
            contactButtonEdit.setOnClickListener { _presenter.onActionEdit() }
            contactButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        animateLayout()
    }

    override fun call() {
        _contact.number?.let { _activity.call(it) }
    }

    override fun sms() {
        _activity.smsContact(_contact)
    }

    override fun edit() {
        _activity.editContact(_contact)
    }

    override fun open() {
        _activity.openContact(_contact)
    }

    override fun delete() {
        _activity.deleteContact(_contact)
    }

    override fun animateLayout() {
        _binding.apply {
            animateViews(views = arrayOf(contactTextName, contactTextNumber), isShow = true)
        }
    }
}