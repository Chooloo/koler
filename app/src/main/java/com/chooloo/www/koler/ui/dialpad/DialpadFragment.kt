package com.chooloo.www.koler.ui.dialpad

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.DialpadBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.widgets.DialpadKey

class DialpadFragment : BaseFragment(), DialpadContract.View {
    private lateinit var _presenter: DialpadController<DialpadFragment>
    private var _onTextChangedListener: (text: String?) -> Unit? = { _ -> }
    private val _binding by lazy { DialpadBinding.inflate(layoutInflater) }
    private var _onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit? = { _, _ -> }
    private val _suggestionsFragment by lazy {
        ContactsFragment.newInstance(
            isSearchable = true,
            isHideNoResults = true
        )
    }

    override val isDialer by lazy { args.getBoolean(ARG_IS_DIALER) }

    override val suggestionsCount: Int
        get() = _suggestionsFragment.presenter.adapter.itemCount

    override var number: String
        get() = _binding.dialpadEditText.text.toString()
        set(value) {
            _binding.dialpadEditText.setText(value)
        }

    override var isSuggestionsVisible: Boolean
        get() = _binding.dialpadSuggestionsScrollView.visibility == VISIBLE
        set(value) {
            if (value && !isSuggestionsVisible) {
                component.animations.show(
                    _binding.dialpadSuggestionsScrollView,
                    true
                )
            } else if (!value && isSuggestionsVisible) {
                _binding.dialpadSuggestionsScrollView.visibility = GONE
            }
        }

    override var isAddContactButtonVisible: Boolean
        get() = _binding.dialpadButtonAddContact.visibility == VISIBLE
        set(value) {
            if (value && !isAddContactButtonVisible) {
                component.animations.show(_binding.dialpadButtonAddContact, true)
            } else if (!value && isAddContactButtonVisible) {
                component.animations.hide(
                    _binding.dialpadButtonAddContact,
                    ifVisible = true,
                    goneOrInvisible = false
                )
            }
        }

    override var isDeleteButtonVisible: Boolean
        get() = _binding.dialpadButtonDelete.visibility == VISIBLE
        set(value) {
            if (value && !isDeleteButtonVisible) {
                component.animations.show(_binding.dialpadButtonDelete, true)
            } else if (!value && isDeleteButtonVisible) {
                component.animations.hide(
                    _binding.dialpadButtonDelete,
                    ifVisible = true,
                    goneOrInvisible = false
                )
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _presenter = DialpadController(this)
        _binding.apply {
            dialpadButtonAddContact.setOnClickListener { _presenter.onAddContactClick() }
            dialpadButtonCall.apply {
                visibility = if (isDialer) VISIBLE else GONE
                setOnClickListener { _presenter.onCallClick() }
            }
            dialpadButtonDelete.apply {
                setOnClickListener { _presenter.onDeleteClick() }
                setOnLongClickListener { _presenter.onLongDeleteClick() }
            }
            dialpadEditText.apply {
                isClickable = isDialer
                isLongClickable = isDialer
                isFocusableInTouchMode = isDialer
                isCursorVisible = isDialer

                if (isDialer) {
                    addTextChangedListener(PhoneNumberFormattingTextWatcher())
                }

                setText(args.getString(ARG_NUMBER))
                addOnTextChangedListener {
                    _presenter.onTextChanged(it)
                    _onTextChangedListener.invoke(it)
                }
            }

            View.OnClickListener {
                (it as DialpadKey).keyCode.also { keyCode ->
                    _presenter.onKeyClick(keyCode)
                    _onKeyDownListener.invoke(keyCode, KeyEvent(ACTION_DOWN, keyCode))
                }
            }
                .also {
                    key0.setOnClickListener(it)
                    key1.setOnClickListener(it)
                    key2.setOnClickListener(it)
                    key3.setOnClickListener(it)
                    key4.setOnClickListener(it)
                    key5.setOnClickListener(it)
                    key6.setOnClickListener(it)
                    key7.setOnClickListener(it)
                    key8.setOnClickListener(it)
                    key9.setOnClickListener(it)
                    keyHex.setOnClickListener(it)
                    keyStar.setOnClickListener(it)
                }

            View.OnLongClickListener {
                _presenter.onLongKeyClick((it as DialpadKey).keyCode)
            }
                .also {
                    key0.setOnLongClickListener(it)
                    key1.setOnLongClickListener(it)
                    key2.setOnLongClickListener(it)
                    key3.setOnLongClickListener(it)
                    key4.setOnLongClickListener(it)
                    key5.setOnLongClickListener(it)
                    key6.setOnLongClickListener(it)
                    key7.setOnLongClickListener(it)
                    key8.setOnLongClickListener(it)
                    key9.setOnLongClickListener(it)
                    keyHex.setOnLongClickListener(it)
                    keyStar.setOnLongClickListener(it)
                }

            _presenter.onTextChanged(dialpadEditText.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager
            .beginTransaction()
            .add(_binding.dialpadSuggestionsContainer.id, _suggestionsFragment)
            .commitNow()
    }

    override fun onResume() {
        super.onResume()
        _suggestionsFragment.presenter.setOnItemsChangedListener(_presenter::onSuggestionsChanged)
    }

    override fun invokeKey(keyCode: Int) {
        _binding.dialpadEditText.onKeyDown(keyCode, KeyEvent(ACTION_DOWN, keyCode))
    }

    override fun setSuggestionsFilter(filter: String) {
        _suggestionsFragment.applyFilter(filter)
    }

    //endregion


    fun setOnTextChangedListener(onTextChangedListener: (text: String?) -> Unit?) {
        _onTextChangedListener = onTextChangedListener
    }

    fun setOnKeyDownListener(onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit?) {
        _onKeyDownListener = onKeyDownListener
    }


    companion object {
        const val TAG = "dialpad_bottom_dialog_fragment"
        const val ARG_IS_DIALER = "dialer"
        private const val ARG_NUMBER = "number"

        fun newInstance(isDialer: Boolean, number: String? = null) = DialpadFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_DIALER, isDialer)
                putString(ARG_NUMBER, number)
            }
        }
    }

}