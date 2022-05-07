package com.chooloo.www.kontacts.ui.contact

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.accounts.AccountsFragment
import com.chooloo.www.kontacts.databinding.ContactBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : BaseFragment<ContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: ContactViewState by activityViewModels()

    private val binding by lazy { ContactBinding.inflate(layoutInflater) }
    private val accountsFragment by lazy { fragmentFactory.getAccountsFragment() }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var callNavigations: CallNavigationsInteractor


    override fun onSetup() {
        viewState.apply {
            contactId.observe(this@ContactFragment) {
                setRawContactsFragment(accountsFragment)
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

            showHistoryEvent.observe(this@ContactFragment) {
                it.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentsFragment(it))
                }
            }
        }

        binding.apply {
            contactButtonSms.setOnClickListener { viewState.onSmsClick() }
            contactButtonCall.setOnClickListener { viewState.onCallClick() }
            contactButtonEdit.setOnClickListener { viewState.onEditClick() }
            contactButtonDelete.setOnClickListener { viewState.onDeleteClick() }
            contactButtonHistory.setOnClickListener { viewState.onHistoryClick() }
            contactButtonWhatsapp.setOnClickListener { viewState.onWhatsappClick() }
        }

        arguments?.getLong(ARG_CONTACT_ID)?.let { viewState.onContactId(it) }
    }

    private fun setRawContactsFragment(accountsFragment: AccountsFragment) {
        childFragmentManager
            .beginTransaction()
            .replace(binding.contactPhonesContainer.id, accountsFragment)
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