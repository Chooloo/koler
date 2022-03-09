package com.chooloo.www.chooloolib.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import javax.inject.Inject

abstract class BaseFragment<out VM : BaseViewState> : Fragment(), BaseView<VM> {
    private var _onFinishListener: () -> Unit = {}

    val args get() = arguments ?: Bundle()

    @Inject lateinit var baseActivity: BaseActivity<*>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetup()
        viewState.apply {
            attach()

            errorEvent.observe(this@BaseFragment) {
                it.ifNew?.let(this@BaseFragment::showError)
            }

            finishEvent.observe(this@BaseFragment) {
                it.ifNew?.let { finish() }
            }

            messageEvent.observe(this@BaseFragment) {
                it.ifNew?.let(this@BaseFragment::showMessage)
            }
        }

        baseActivity.onFragmentSetup(this)
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.viewState.errorEvent.call(stringResId)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.viewState.messageEvent.call(stringResId)
    }

    override fun finish() {
        super.finish()
        _onFinishListener.invoke()
    }

    fun setOnFinishListener(onFinishListener: () -> Unit) {
        _onFinishListener = onFinishListener
    }


    abstract val contentView: View
    abstract override val viewState: VM
}