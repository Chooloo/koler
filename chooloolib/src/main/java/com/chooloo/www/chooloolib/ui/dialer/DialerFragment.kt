package com.chooloo.www.chooloolib.ui.dialer

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.ui.contacts.ContactsSuggestionsViewState
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DialerFragment @Inject constructor() : DialpadFragment() {
    override val viewState: DialerViewState by viewModels()

    private val suggestionsViewState: ContactsSuggestionsViewState by activityViewModels()
    private val _suggestionsFragment by lazy { fragmentFactory.getContactsSuggestionsFragment() }

    @Inject lateinit var callNavigations: CallNavigationsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory


    override fun onSetup() {
        super.onSetup()
        binding.apply {
            dialpadButtonCall.isVisible = true

            dialpadButtonDelete.apply {
                setOnClickListener {
                    viewState.onDeleteClick()
                }
                setOnLongClickListener {
                    viewState.onLongDeleteClick()
                }
            }

            dialpadButtonAddContact.setOnClickListener {
                viewState.onAddContactClick()
            }

            dialpadButtonCall.apply {
                visibility = View.VISIBLE
                setOnClickListener { viewState.onCallClick() }
            }

            dialpadEditText.apply {
                isClickable = true
                isCursorVisible = true
                isLongClickable = true
                isFocusableInTouchMode = true

                addTextChangedListener(PhoneNumberFormattingTextWatcher())
            }
        }

        viewState.apply {
            text.observe(this@DialerFragment) {
                suggestionsViewState.onFilterChanged(it)
            }

            isSuggestionsVisible.observe(this@DialerFragment) {
                if (it && !binding.dialpadSuggestionsScrollView.isVisible) {
                    animationsInteractor.show(binding.dialpadSuggestionsScrollView, true)
                } else if (!it && binding.dialpadSuggestionsScrollView.isVisible) {
                    animationsInteractor.hide(binding.dialpadSuggestionsScrollView, true, true)
                }
            }

            isAddContactButtonVisible.observe(this@DialerFragment) {
                if (it && !binding.dialpadButtonAddContact.isVisible) {
                    animationsInteractor.show(binding.dialpadButtonAddContact, true)
                } else if (!it && binding.dialpadButtonAddContact.isVisible) {
                    animationsInteractor.hide(binding.dialpadButtonAddContact, true, false)
                }
            }

            isDeleteButtonVisible.observe(this@DialerFragment) {
                if (it && !binding.dialpadButtonDelete.isVisible) {
                    animationsInteractor.show(binding.dialpadButtonDelete, true)
                } else if (!it && binding.dialpadButtonDelete.isVisible) {
                    animationsInteractor.hide(
                        binding.dialpadButtonDelete,
                        ifVisible = true,
                        goneOrInvisible = false
                    )
                }
            }

            callNumberEvent.observe(this@DialerFragment) {
                it.contentIfNew?.let(callNavigations::call)
            }

            callVoicemailEvent.observe(this@DialerFragment) {
                it.contentIfNew?.let { callNavigations.callVoicemail() }
            }
        }

        suggestionsViewState.itemsChangedEvent.observe(this@DialerFragment) {
            it.peekContent()?.let(viewState::onSuggestionsChanged)
        }

        args.getString(ARG_NUMBER)?.let { it.forEach(viewState::onCharClick) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager
            .beginTransaction()
            .add(binding.dialpadSuggestionsContainer.id, _suggestionsFragment)
            .commitNow()
    }

    companion object {
        private const val ARG_NUMBER = "number"

        fun newInstance(text: String? = null) = DialerFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NUMBER, text)
            }
        }
    }
}