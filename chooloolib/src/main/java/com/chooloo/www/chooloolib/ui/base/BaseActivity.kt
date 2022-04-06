package com.chooloo.www.chooloolib.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewState> : AppCompatActivity(), BaseView<VM> {
    @Inject lateinit var strings: StringsInteractor
    @Inject lateinit var disposables: CompositeDisposable
    @Inject lateinit var preferences: PreferencesInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        applicationContext.setTheme(preferences.accentTheme.theme)
        setTheme(preferences.accentTheme.theme)
        AppCompatDelegate.setDefaultNightMode(preferences.themeMode.mode)
        contentView?.let { setContentView(it) }
        onSetup()
        viewState.apply {
            attach()

            errorEvent.observe(this@BaseActivity) {
                it.ifNew?.let(this@BaseActivity::showError)
            }

            finishEvent.observe(this@BaseActivity) {
                it.ifNew?.let { finish() }
            }

            messageEvent.observe(this@BaseActivity) {
                it.ifNew?.let(this@BaseActivity::showMessage)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewState.detach()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun finish() {
        finishAndRemoveTask()
    }

    override fun showError(stringResId: Int) {
        Toast.makeText(
            applicationContext,
            strings.getString(stringResId),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(
            this,
            strings.getString(stringResId),
            Toast.LENGTH_SHORT
        ).show()
    }

    open fun <VS : BaseViewState> onFragmentSetup(fragment: BaseFragment<VS>) {}

    abstract val contentView: View?
    abstract override val viewState: VM
}