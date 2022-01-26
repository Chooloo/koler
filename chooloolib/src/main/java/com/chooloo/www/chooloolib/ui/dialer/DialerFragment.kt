package com.chooloo.www.chooloolib.ui.dialer

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import com.chooloo.www.chooloolib.ui.contacts.ContactsSuggestionsFragment
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import javax.inject.Inject

class DialerFragment : DialpadFragment(), DialerContract.View {
    private val _suggestionsFragment by lazy { ContactsSuggestionsFragment.newInstance() }

    @Inject override lateinit var controller: DialerContract.Controller<DialerFragment>


    override val suggestionsCount: Int
        get() = _suggestionsFragment.controller.adapter.itemCount

    override var isSuggestionsVisible: Boolean
        get() = binding.dialpadSuggestionsScrollView.visibility == View.VISIBLE
        set(value) {
            if (value && !isSuggestionsVisible) {
                animationsInteractor.show(binding.dialpadSuggestionsScrollView, true)
            } else if (!value && isSuggestionsVisible) {
                animationsInteractor.hide(binding.dialpadSuggestionsScrollView, true, true)
            }
        }

    override var isAddContactButtonVisible: Boolean
        get() = binding.dialpadButtonAddContact.visibility == View.VISIBLE
        set(value) {
            if (value && !isAddContactButtonVisible) {
                animationsInteractor.show(binding.dialpadButtonAddContact, true)
            } else if (!value && isAddContactButtonVisible) {
                animationsInteractor.hide(binding.dialpadButtonAddContact, true, false)
            }
        }


    override fun onSetup() {
        super.onSetup()
        binding.apply {
            dialpadButtonCall.apply {
                visibility = View.VISIBLE
                setOnClickListener { controller.onCallClick() }
            }
            dialpadButtonAddContact.setOnClickListener { controller.onAddContactClick() }
            dialpadEditText.apply {
                isClickable = true
                isCursorVisible = true
                isLongClickable = true
                isFocusableInTouchMode = true

                setText(args.getString(ARG_NUMBER))
                addTextChangedListener(PhoneNumberFormattingTextWatcher())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        _suggestionsFragment.controller.setOnItemsChangedListener(controller::onSuggestionsChanged)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager
            .beginTransaction()
            .add(binding.dialpadSuggestionsContainer.id, _suggestionsFragment)
            .commitNow()
    }

    override fun setSuggestionsFilter(filter: String) {
        _suggestionsFragment.applyFilter(filter)
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