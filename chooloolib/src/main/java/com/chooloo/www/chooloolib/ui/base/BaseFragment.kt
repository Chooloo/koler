package com.chooloo.www.chooloolib.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseContract.View {
    private var _onFinishListener: () -> Unit = {}

    override val component get() = baseActivity.component

    protected val baseActivity by lazy { context as BaseActivity }

    val args: Bundle
        get() = arguments ?: Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        assert(context is BaseActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }


    override fun showError(@StringRes stringResId: Int) {
        baseActivity.showError(stringResId)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.showMessage(stringResId)
    }

    override fun finish() {
        super.finish()
        _onFinishListener.invoke()
    }


    fun setOnFinishListener(onFinishListener: () -> Unit) {
        _onFinishListener = onFinishListener
    }


    abstract val contentView: View
}