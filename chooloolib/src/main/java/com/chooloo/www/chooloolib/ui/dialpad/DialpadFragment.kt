package com.chooloo.www.chooloolib.ui.dialpad

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.databinding.DialpadBinding
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.widgets.DialpadKey
import com.chooloo.www.chooloolib.ui.widgets.TextContextMenuItemListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class DialpadFragment @Inject constructor() : BaseFragment<DialpadViewState>(), TextContextMenuItemListener {
    override val contentView by lazy { binding.root }
    override val viewState: DialpadViewState by activityViewModels()

    @Inject lateinit var animationsInteractor: AnimationsInteractor

    protected val binding by lazy { DialpadBinding.inflate(layoutInflater) }

    override fun onSetup() {
        binding.apply {
            dialpadButtonCall.isVisible = false
            dialpadButtonDelete.isVisible = false

            dialpadEditText.apply {
                isClickable = false
                isLongClickable = false
                isCursorVisible = false
                isFocusableInTouchMode = false

                addTextContextMenuItemListener(this@DialpadFragment)
            }

            View.OnClickListener {
                viewState.onCharClick((it as DialpadKey).char)
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
                viewState.onLongKeyClick((it as DialpadKey).char)
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
        }

        viewState.text.observe(this@DialpadFragment, binding.dialpadEditText::setText)
    }

    override fun onPaste() {
        viewState.onTextPasted()
    }
}