package com.chooloo.www.chooloolib.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.chooloolib.di.factory.controller.ControllerFactory
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    @Inject lateinit var disposables: CompositeDisposable
    @Inject lateinit var stringsInteractor: StringsInteractor
    @Inject lateinit var controllerFactory: ControllerFactory
    @Inject lateinit var preferencesInteractor: PreferencesInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(preferencesInteractor.accentTheme.theme)
        contentView?.let { setContentView(it) }
    }

    override fun onStart() {
        super.onStart()
        onSetup()
        controller.onSetup()
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
    }

    override fun showError(stringResId: Int) {
        Toast.makeText(
            applicationContext,
            stringsInteractor.getString(stringResId),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(
            this,
            stringsInteractor.getString(stringResId),
            Toast.LENGTH_SHORT
        ).show()
    }

    abstract val contentView: View?
}