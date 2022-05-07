package com.chooloo.www.chooloolib.ui.briefcontact

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.BriefContactBinding
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.accounts.AccountsViewState
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BriefContactFragment @Inject constructor() : BaseFragment<BriefContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: BriefContactViewState by viewModels()

    protected val binding by lazy { BriefContactBinding.inflate(layoutInflater) }
    private val phonesViewState: PhonesViewState by activityViewModels()
    private val accountsViewState: AccountsViewState by activityViewModels()
    private val phonesFragment by lazy { fragmentFactory.getPhonesFragment(viewState.contactId.value) }
    private val accountsFragment by lazy { fragmentFactory.getAccountsFragment(viewState.contactId.value) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var dialogs: DialogsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var permissions: PermissionsInteractor
    @Inject lateinit var callNavigations: CallNavigationsInteractor


    override fun onSetup() {
        binding.apply {
            briefContactButtonSms.setOnClickListener {
                viewState.onActionSms()
            }

            contactButtonCall.setOnClickListener {
                viewState.onActionCall()
            }

            briefContactButtonEdit.setOnClickListener {
                viewState.onActionEdit()
            }

            briefContactButtonDelete.setOnClickListener {
                viewState.onActionDelete()
            }

            briefContactStarButton.setOnClickListener {
                viewState.onActionStar(it.isActivated)
            }

            briefContactButtonHistory.setOnClickListener {
                viewState.onActionHistory()
            }
        }

        viewState.apply {
            contactName.observe(this@BriefContactFragment) {
                binding.briefContactTextName.text = it
            }

            contactImage.observe(this@BriefContactFragment) {
                binding.briefContactImage.apply {
                    setImageURI(it)
                    isVisible = it != null
                }
            }

            isStarIconVisible.observe(this@BriefContactFragment) {
                binding.briefContactStarButton.isVisible = it
            }

            isStarIconActivated.observe(this@BriefContactFragment) {
                binding.briefContactStarButton.isActivated = it
            }

            callEvent.observe(this@BriefContactFragment) {
                it.ifNew?.let(callNavigations::call)
            }

            confirmContactDeleteEvent.observe(this@BriefContactFragment) {
                it.ifNew?.let {
                    dialogs.askForValidation(R.string.explain_delete_contact) { bl ->
                        if (bl) onDeleteConfirmed()
                    }
                }
            }

            showHistoryEvent.observe(this@BriefContactFragment) { ev ->
                ev.ifNew?.let { prompts.showFragment(fragmentFactory.getRecentsFragment(it)) }
            }

            onContactId(args.getLong(ARG_CONTACT_ID))
        }

        childFragmentManager
            .beginTransaction()
            .replace(binding.briefContactAccountsFragmentContainer.id, accountsFragment)
            .commitNow()

        childFragmentManager
            .beginTransaction()
            .replace(binding.briefContactPhonesFragmentContainer.id, phonesFragment)
            .commitNow()

        accountsViewState.isEmpty.observe(this@BriefContactFragment) {
            binding.briefContactAccountsFragmentContainer.isVisible = !it
        }

        phonesViewState.isEmpty.observe(this@BriefContactFragment) {
            binding.briefContactPhonesFragmentContainer.isVisible = !it
        }
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