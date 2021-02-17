package com.chooloo.www.koler.ui.dialpad

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class DialpadFragment : BaseFragment(), DialpadMvpView {

    override val isDialer = argsSafely.getBoolean(ARG_DIALER)
    private var _onKeyDownListener: ((keyCode: Int, event: KeyEvent) -> Unit?)? = null
    private lateinit var _presenter: DialpadMvpPresenter<DialpadMvpView>
    private lateinit var _searchViewModel: SearchViewModel
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

        _searchViewModel = ViewModelProvider(_activity).get(SearchViewModel::class.java)

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
        _searchViewModel.number.value = if (number == "") null else number
    }

    override fun backspace() {
        val number = _binding.dialpadEditText.numbers
        if (number.isNotEmpty()) {
            _binding.dialpadEditText.setText(number.substring(0, number.length - 1))
        }
    }

    override fun call() {
        if (number.isEmpty()) {
            Toast.makeText(context, getString(R.string.please_enter_a_number), Toast.LENGTH_SHORT).show()
        } else {
            _activity.call(_binding.dialpadEditText.numbers)
        }
    }

    override fun callVoicemail() {
        _activity.callVoicemail()
    }

    override fun addContact() {
        _activity.addContact(_binding.dialpadEditText.numbers)
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