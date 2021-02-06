package com.chooloo.www.callmanager.ui.dialpad

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.FragmentDialpadBinding
import com.chooloo.www.callmanager.service.CallManager.call
import com.chooloo.www.callmanager.service.CallManager.callVoicemail
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.ui.widgets.DialpadKey
import com.chooloo.www.callmanager.util.*
import com.chooloo.www.callmanager.viewmodel.dial.DialViewModel

class DialpadFragment : BaseFragment(), DialpadMvpView {

    override val isDialer: Boolean
    private var _onKeyDownListener: ((keyCode: Int, event: KeyEvent) -> Unit?)? = null
    private lateinit var _presenter: DialpadMvpPresenter<DialpadMvpView>
    private lateinit var _dialViewModel: DialViewModel
    private lateinit var _binding: FragmentDialpadBinding

    companion object {
        const val TAG = "dialpad_bottom_dialog_fragment"
        const val ARG_DIALER = "dialer"

        fun newInstance(isDialer: Boolean): DialpadFragment {
            return DialpadFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_DIALER, isDialer)
                }
            }
        }
    }

    init {
        isDialer = argsSafely.getBoolean(ARG_DIALER)
    }

    override var number: String
        get() = _binding.dialpadEditText.text.toString()
        set(value) = _binding.dialpadEditText.setText(value)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDialpadBinding.inflate(inflater)
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = DialpadPresenter()
        _presenter.attach(this)

        _dialViewModel = ViewModelProvider(_activity).get(DialViewModel::class.java)

        _binding.apply {
            dialpadEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher(resources.configuration.locales.get(0).toString()))
            dialpadEditText.addTextChangedListener(
                    beforeTextChanged = { _, _, _, _ -> },
                    afterTextChanged = { },
                    onTextChanged = { s, _, _, _ -> _presenter.onTextChanged(s.toString()) }
            )

            View.OnClickListener { view: View -> _presenter.onKeyClick((view as DialpadKey).keyCode) }.also {
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
            key0.setOnLongClickListener { _presenter.onLongZeroClick() }
            key1.setOnLongClickListener { _presenter.onLongOneClick() }
            dialpadButtonDelete.setOnClickListener { _presenter.onDeleteClick() }
            dialpadButtonDelete.setOnLongClickListener { _presenter.onLongDeleteClick() }
            dialpadButtonAddContact.setOnClickListener { _presenter.onAddContactClick() }
        }
    }

    override fun showDeleteButton(isShow: Boolean) {
        if (_binding.dialpadButtonDelete.visibility == (if (isShow) View.GONE else View.VISIBLE)) {
            showView(_binding.dialpadButtonDelete, isShow)
        }
    }

    override fun showAddContactButton(isShow: Boolean) {
        if (_binding.dialpadButtonAddContact.visibility == (if (isShow) View.GONE else View.VISIBLE)) {
            showView(_binding.dialpadButtonAddContact, isShow)
        }
    }

    override fun toggleDialerView(isToggle: Boolean) {
        _binding.apply {
            dialpadButtonDelete.visibility = if (isToggle) View.VISIBLE else View.GONE
            dialpadButtonCall.visibility = if (isToggle) View.VISIBLE else View.GONE
            dialpadEditText.isClickable = isToggle
            dialpadEditText.isLongClickable = isToggle
            dialpadEditText.isFocusableInTouchMode = isToggle
            dialpadEditText.isCursorVisible = isToggle
        }
    }

    override fun registerKeyEvent(keyCode: Int) {
        KeyEvent(KeyEvent.ACTION_DOWN, keyCode).also {
            _binding.dialpadEditText.onKeyDown(keyCode, it)
            _onKeyDownListener?.invoke(keyCode, it)
        }
    }

    override fun setViewModelNumber(number: String?) {
        _dialViewModel.number.value = if (number == "") null else number
    }

    override fun backspace() {
        val number = _binding.dialpadEditText.numbers
        if (number.isNotEmpty()) {
            _binding.dialpadEditText.setText(number.substring(0, number.length - 1))
        }
    }

    override fun call() {
        val number = _dialViewModel.number.value
        if (number?.isEmpty() == true) {
            Toast.makeText(context, getString(R.string.please_enter_a_number), Toast.LENGTH_SHORT).show()
        } else {
            call(_activity, _binding.dialpadEditText.numbers)
        }
    }

    override fun callVoicemail() {
        callVoicemail(_activity)
    }

    override fun addContact() {
        addContact(_activity, lookupContact(_activity, _binding.dialpadEditText.numbers))
    }

    override fun vibrate() {
        _activity.vibrate(SHORT_VIBRATE_LENGTH)
    }

    override fun playTone(keyCode: Int) {
        context?.playToneByKey(keyCode)
    }

    fun setOnKeyDownListener(onKeyDownListener: ((keyCode: Int, event: KeyEvent) -> Unit?)?) {
        _onKeyDownListener = onKeyDownListener
    }
}