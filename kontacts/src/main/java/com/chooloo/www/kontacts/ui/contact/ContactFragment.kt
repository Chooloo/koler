package com.chooloo.www.kontacts.ui.contact

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment
import com.chooloo.www.kontacts.databinding.ContactBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : BaseFragment<ContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: ContactViewState by activityViewModels()

    private val binding by lazy { ContactBinding.inflate(layoutInflater) }
    private val phonesFragment by lazy { fragmentFactory.getPhonesFragment() }

    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var callNavigations: CallNavigationsInteractor


    override fun onSetup() {
        viewState.apply {
            contactId.observe(this@ContactFragment) {
                setPhonesFragment(phonesFragment)
            }

            callEvent.observe(this@ContactFragment) {
                it.ifNew?.let(callNavigations::call)
            }

            contactName.observe(this@ContactFragment) {
                binding.contactName.text = it
            }

            contactImage.observe(this@ContactFragment) {
                binding.contactImage.setImageURI(it)
            }
        }

        binding.apply {
            contactButtonSms.setOnClickListener { viewState.onSmsClick() }
            contactButtonCall.setOnClickListener { viewState.onCallClick() }
            contactButtonEdit.setOnClickListener { viewState.onEditClick() }
            contactButtonDelete.setOnClickListener { viewState.onDeleteClick() }
        }

        arguments?.getLong(ARG_CONTACT_ID)?.let { viewState.onContactId(it) }
    }

    private fun setPhonesFragment(phonesFragment: PhonesFragment) {
        childFragmentManager
            .beginTransaction()
            .replace(binding.contactPhonesContainer.id, phonesFragment)
            .commit()
    }

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}