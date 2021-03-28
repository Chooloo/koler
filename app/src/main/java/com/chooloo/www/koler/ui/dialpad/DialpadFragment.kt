package com.chooloo.www.koler.ui.dialpad

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.FragmentDialpadBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.widgets.DialpadKey
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.call.callVoicemail
import com.chooloo.www.koler.viewmodel.SearchViewModel

class DialpadFragment : BaseFragment(), DialpadContract.View {
    override val isDialer by lazy { argsSafely.getBoolean(ARG_IS_DIALER) }
    private val _presenter by lazy { DialpadPresenter<DialpadContract.View>() }
    private val _binding by lazy { FragmentDialpadBinding.inflate(layoutInflater) }
    private var _onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit? = { _, _ -> }
    private val _searchViewModel by lazy { ViewModelProvider(_activity).get(SearchViewModel::class.java) }

    companion object {
        const val ARG_IS_DIALER = "dialer"
        private const val ARG_NUMBER = "number"
        const val TAG = "dialpad_bottom_dialog_fragment"

        fun newInstance(isDialer: Boolean, number: String? = null) = DialpadFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_DIALER, isDialer)
                putString(ARG_NUMBER, number)
            }
        }
    }

    override var number: String
        get() = _binding.dialpadEditText.text.toString()
        set(value) {
            _binding.dialpadEditText.setText(value)
        }

    override var isAddContactButtonVisible: Boolean
        get() = _binding.dialpadButtonAddContact.visibility == VISIBLE
        set(value) {
            if (_binding.dialpadButtonAddContact.visibility == (if (value) GONE else VISIBLE)) {
                showView(_binding.dialpadButtonAddContact, value)
            }
        }

    override var isDeleteButtonVisible: Boolean
        get() = _binding.dialpadButtonAddContact.visibility == VISIBLE
        set(value) {
            if (_binding.dialpadButtonDelete.visibility == (if (value) GONE else VISIBLE)) {
                showView(_binding.dialpadButtonDelete, value)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _presenter.attach(this)

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

                setText(argsSafely.getString(ARG_NUMBER))
                addTextChangedListener(
                    PhoneNumberFormattingTextWatcher(
                        resources.configuration.locales.get(0).toString()
                    )
                )
                addTextChangedListener(
                    { _, _, _, _ -> },
                    { s, _, _, _ -> _presenter.onTextChanged(s.toString()) },
                    {})
            }

            View.OnClickListener { _presenter.onKeyClick((it as DialpadKey).keyCode) }
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

            View.OnLongClickListener { _presenter.onLongKeyClick((it as DialpadKey).keyCode) }
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

            _presenter.onTextChanged(_searchViewModel.number.value)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    //region phone actions
    override fun call() {
        if (number.isEmpty()) {
            _activity.showMessage(R.string.error_enter_number)
        } else {
            _activity.call(_binding.dialpadEditText.numbers)
        }
    }

    override fun addContact() {
        _activity.addContact(_binding.dialpadEditText.numbers)
    }

    override fun callVoicemail() {
        _activity.callVoicemail()
    }
    //endregion

    //region key actions
    override fun vibrate() {
        _activity.vibrate(SHORT_VIBRATE_LENGTH)
    }

    override fun playTone(keyCode: Int) {
        context?.playToneByKey(keyCode)
    }

    override fun registerKeyEvent(keyCode: Int) {
        KeyEvent(KeyEvent.ACTION_DOWN, keyCode).also {
            _binding.dialpadEditText.onKeyDown(keyCode, it)
            _onKeyDownListener.invoke(keyCode, it)
        }
    }
    //endregion

    override fun backspace() {
        val number = _binding.dialpadEditText.numbers
        if (number.isNotEmpty()) {
            _binding.dialpadEditText.setText(number.substring(0, number.length - 1))
        }
    }

    override fun setViewModelNumber(number: String?) {
        _searchViewModel.number.value = if (number == "") null else number
    }

    fun setOnKeyDownListener(onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit?) {
        _onKeyDownListener = onKeyDownListener
    }
}