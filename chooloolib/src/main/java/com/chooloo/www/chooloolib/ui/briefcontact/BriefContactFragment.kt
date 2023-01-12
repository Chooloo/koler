package com.chooloo.www.chooloolib.ui.briefcontact

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.BriefContactBinding
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.telecom.TelecomInteractor
import com.chooloo.www.chooloolib.ui.accounts.AccountsViewState
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.briefcontact.menu.BriefContactMenuViewState
import com.chooloo.www.chooloolib.ui.phones.PhonesViewState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BriefContactFragment @Inject constructor() : BaseFragment<BriefContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: BriefContactViewState by viewModels()

    protected val binding by lazy { BriefContactBinding.inflate(layoutInflater) }
    private val phonesViewState: PhonesViewState by activityViewModels()
    private val accountsViewState: AccountsViewState by activityViewModels()
    private val menuViewState: BriefContactMenuViewState by activityViewModels()
    private val phonesFragment by lazy { fragmentFactory.getPhonesFragment(viewState.contactId.value) }
    private val accountsFragment by lazy { fragmentFactory.getAccountsFragment(viewState.contactId.value) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var dialogs: DialogsInteractor
    @Inject lateinit var telecomInteractor: TelecomInteractor


    override fun onSetup() {
        binding.briefContactImage.isVisible = false

        binding.apply {
            briefContactButtonMore.setOnClickListener {
                viewState.onMoreClick()
            }

            briefContactMainActions.briefContactButtonSms.setOnClickListener {
                viewState.onSmsClick()
            }

            briefContactMainActions.contactButtonCall.setOnClickListener {
                viewState.onCallClick()
            }

            briefContactMainActions.briefContactButtonEdit.setOnClickListener {
                viewState.onEditClick()
            }
        }

        viewState.apply {
            isFavorite.observe(this@BriefContactFragment) {
                menuViewState.onIsFavorite(it)
                binding.briefContactIconFav.isVisible = it
            }

            contactName.observe(this@BriefContactFragment) {
                binding.briefContactTextName.text = it
                it?.let(menuViewState::onContactName)
            }

            contactNumber.observe(this@BriefContactFragment) {
                binding.briefContactTextNumber.text = it
            }

            contactImage.observe(this@BriefContactFragment) {
                binding.briefContactImage.clearColorFilter()
                binding.briefContactImage.backgroundTintList = null
                binding.briefContactImage.imageTintList = null
                binding.briefContactImage.isVisible = true
                Picasso.with(baseActivity).load(it).into(binding.briefContactImage)
            }

            callEvent.observe(this@BriefContactFragment) {
                it.ifNew?.let(telecomInteractor::callNumber)
            }

            showMoreEvent.observe(this@BriefContactFragment) {
                it.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getBriefContactMenuFragment())
                }
            }

            onContactId(args.getLong(ARG_CONTACT_ID))

            menuViewState.apply {
                finishEvent.observe(this@BriefContactFragment) {
                    viewState.onFinish()
                }

                onContactId(args.getLong(ARG_CONTACT_ID))
            }
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