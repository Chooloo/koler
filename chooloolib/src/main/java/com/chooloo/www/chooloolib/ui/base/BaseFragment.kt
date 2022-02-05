package com.chooloo.www.chooloolib.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.chooloo.www.chooloolib.di.factory.controller.ControllerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment(), BaseContract.View, LifecycleObserver {
    private var _onFinishListener: () -> Unit = {}

    @Inject lateinit var baseActivity: BaseActivity
    @Inject lateinit var controllerFactory: ControllerFactory


    val args get() = arguments ?: Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        assert(context is BaseActivity)
        (context as BaseActivity).lifecycle.addObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetup()
        controller.onSetup()
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