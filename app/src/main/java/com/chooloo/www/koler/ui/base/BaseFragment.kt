package com.chooloo.www.koler.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent

abstract class BaseFragment : Fragment(), BaseContract.View {
    override val component: ActivityComponent
        get() = baseActivity.component

    protected val baseActivity by lazy { context as BaseActivity }

    val args: Bundle
        get() = arguments ?: Bundle()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
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
}