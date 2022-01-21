package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.databinding.BriefContactBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment

open class BriefContactFragment : BaseFragment(), BriefContactContract.View {
    override val contentView by lazy { binding.root }
    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }

    private lateinit var controller: BriefContactController<BriefContactFragment>
    protected val binding by lazy { BriefContactBinding.inflate(layoutInflater) }
    protected val phonesFragment by lazy { PhonesFragment.newInstance(contactId) }

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
        controller = BriefContactController(this)

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
        const val TAG = "contact_fragment"
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = BriefContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}