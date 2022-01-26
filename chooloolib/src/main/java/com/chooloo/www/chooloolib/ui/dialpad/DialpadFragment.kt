package com.chooloo.www.chooloolib.ui.dialpad

import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.View
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.databinding.DialpadBinding
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.widgets.DialpadKey
import javax.inject.Inject

open class DialpadFragment : BaseFragment(), DialpadContract.View {
    override val contentView by lazy { binding.root }

    private var _onTextChangedListener: (text: String) -> Unit = { _ -> }
    private var _onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit? = { _, _ -> }
    protected val binding by lazy { DialpadBinding.inflate(layoutInflater) }

    @Inject lateinit var animationsInteractor: AnimationsInteractor
    @Inject open lateinit var controller: DialpadContract.Controller<out DialpadFragment>


    override var text: String
        get() = binding.dialpadEditText.text.toString()
        set(value) {
            binding.dialpadEditText.setText(value)
        }

    override var isDeleteButtonVisible: Boolean
        get() = binding.dialpadButtonDelete.isVisible
        set(value) {
            if (value && !isDeleteButtonVisible) {
                animationsInteractor.show(binding.dialpadButtonDelete, true)
            } else if (!value && isDeleteButtonVisible) {
                animationsInteractor.hide(
                    binding.dialpadButtonDelete,
                    ifVisible = true,
                    goneOrInvisible = false
                )
            }
        }


    override fun onSetup() {
        binding.apply {
            dialpadButtonDelete.apply {
                setOnClickListener { controller.onDeleteClick() }
                setOnLongClickListener { controller.onLongDeleteClick() }
            }
            dialpadEditText.apply {
                isClickable = false
                isLongClickable = false
                isCursorVisible = false
                isFocusableInTouchMode = false

                addOnTextChangedListener {
                    controller.onTextChanged(it)
                    _onTextChangedListener.invoke(it)
                }
            }

            View.OnClickListener {
                (it as DialpadKey).keyCode.also { keyCode ->
                    controller.onKeyClick(keyCode)
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
                controller.onLongKeyClick((it as DialpadKey).keyCode)
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

            controller.onTextChanged(dialpadEditText.text.toString())
        }
    }

    override fun invokeKey(keyCode: Int) {
        binding.dialpadEditText.onKeyDown(keyCode, KeyEvent(ACTION_DOWN, keyCode))
    }


    fun setOnTextChangedListener(onTextChangedListener: (text: String) -> Unit) {
        _onTextChangedListener = onTextChangedListener
    }

    fun setOnKeyDownListener(onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit?) {
        _onKeyDownListener = onKeyDownListener
    }


    companion object {
        fun newInstance() = DialpadFragment()
    }
}