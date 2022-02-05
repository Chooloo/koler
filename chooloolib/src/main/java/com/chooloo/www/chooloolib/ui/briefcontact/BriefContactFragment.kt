package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.databinding.BriefContactBinding
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BriefContactFragment @Inject constructor() : BaseFragment(), BriefContactContract.View {
    override val contentView by lazy { binding.root }
    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }
    override lateinit var controller: BriefContactContract.Controller

    protected val binding by lazy { BriefContactBinding.inflate(layoutInflater) }
    private val phonesFragment by lazy { fragmentFactory.getPhonesFragment(contactId) }

    @Inject lateinit var fragmentFactory: FragmentFactory


    override var contactName: String?
        get() = binding.briefContactTextName.text.toString()
        set(value) {
            binding.briefContactTextName.text = value
        }

    override var contactImage: Uri?
        get() = null
        set(value) {
            binding.briefContactImage.apply {
                setImageURI(value)
                visibility = if (value != null) VISIBLE else GONE
            }
        }

    override var isStarIconVisible: Boolean
        get() = binding.contactImageStar.isVisible
        set(value) {
            binding.contactImageStar.isVisible = value
        }


    override fun onSetup() {
        controller = controllerFactory.getBriefContactController(this)
        binding.apply {
            contactButtonSms.setOnClickListener { controller.onActionSms() }
            contactButtonCall.setOnClickListener { controller.onActionCall() }
            contactButtonEdit.setOnClickListener { controller.onActionEdit() }
            contactButtonDelete.setOnClickListener { controller.onActionDelete() }
        }

        childFragmentManager
            .beginTransaction()
            .add(binding.contactPhonesFragmentContainer.id, phonesFragment)
            .commitNow()
    }


    companion object {
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = BriefContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}