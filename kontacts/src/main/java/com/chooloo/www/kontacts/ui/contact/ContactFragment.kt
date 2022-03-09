package com.chooloo.www.kontacts.ui.contact

import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment
import com.chooloo.www.kontacts.R
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
            contactImage.observe(this@ContactFragment, binding.contactImage::setImageURI)

            contactId.observe(this@ContactFragment) {
                setPhonesFragment(fragmentFactory.getPhonesFragment(it))
            }

            contactName.observe(this@ContactFragment) {
                binding.contactName.text = it
            }

            callEvent.observe(this@ContactFragment) {
                it.ifNew?.let(callNavigations::call)
            }

            isEditable.observe(this@ContactFragment) {
                binding.root.transitionToState(if (it) R.id.constraint_set_contact_editable else R.id.constraint_set_contact_default)
            }
        }

        binding.apply {
            contactButtonSms.setOnClickListener { viewState.onSmsClick() }
            contactButtonCall.setOnClickListener { viewState.onCallClick() }
            contactButtonEdit.setOnClickListener { viewState.onEditClick() }
            contactButtonDelete.setOnClickListener { viewState.onDeleteClick() }
        }
    }

    private fun setPhonesFragment(phonesFragment: PhonesFragment) {
        childFragmentManager
            .beginTransaction()
            .replace(binding.contactPhonesContainer.id, phonesFragment)
            .commit()
    }
}