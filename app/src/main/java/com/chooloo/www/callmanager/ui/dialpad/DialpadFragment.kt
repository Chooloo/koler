package com.chooloo.www.callmanager.ui.dialpad

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.FragmentDialpadBinding
import com.chooloo.www.callmanager.service.CallManager.call
import com.chooloo.www.callmanager.service.CallManager.callVoicemail
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.ui.widgets.DialpadKey
import com.chooloo.www.callmanager.util.AnimationUtils.showView
import com.chooloo.www.callmanager.util.AudioUtils
import com.chooloo.www.callmanager.util.AudioUtils.playToneByKey
import com.chooloo.www.callmanager.util.ContactUtils.addContact
import com.chooloo.www.callmanager.util.ContactUtils.lookupContact
import com.chooloo.www.callmanager.util.Utilities
import com.chooloo.www.callmanager.viewmodel.dial.DialViewModel

class DialpadFragment : BaseFragment(), DialpadMvpView {

    override val isDialer: Boolean
    private var _onKeyDownListener: OnKeyDownListener? = null
    private lateinit var _presenter: DialpadMvpPresenter<DialpadMvpView>
    private lateinit var _dialViewModel: DialViewModel
    private lateinit var _binding: FragmentDialpadBinding

    companion object {
        const val TAG = "dialpad_bottom_dialog_fragment"
        const val ARG_DIALER = "dialer"

        @JvmStatic
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

    override fun onResume() {
        super.onResume()
        AudioUtils.toggleToneGenerator(true)
    }

    override fun onPause() {
        super.onPause()
        AudioUtils.apply {
            stopTone()
            toggleToneGenerator(false)
        }
    }

    override fun onSetup() {
        _presenter = DialpadPresenter()
        _presenter.attach(this)

        _dialViewModel = ViewModelProvider(_activity).get(DialViewModel::class.java)

        _binding.dialpadEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher(Utilities.sLocale.country))
        _binding.dialpadEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                _presenter.onTextChanged(s.toString())
            }
        })

        _binding.run {
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
            _onKeyDownListener?.onKeyPressed(keyCode, it)
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
        if (number?.isEmpty() ?: false) {
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
        Utilities.vibrate(_activity, Utilities.SHORT_VIBRATE_LENGTH)
    }

    override fun playTone(keyCode: Int) {
        playToneByKey(keyCode, _activity)
    }

    fun setOnKeyDownListener(onKeyDownListener: OnKeyDownListener?) {
        _onKeyDownListener = onKeyDownListener
    }

    interface OnKeyDownListener {
        fun onKeyPressed(keyCode: Int, event: KeyEvent?)
    }
}