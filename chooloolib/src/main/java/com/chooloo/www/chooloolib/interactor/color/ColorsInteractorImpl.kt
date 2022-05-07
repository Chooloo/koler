package com.chooloo.www.chooloolib.interactor.color

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseObservable<ColorsInteractor.Listener>(), ColorsInteractor {

    private fun getThemedContext(): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.uiMode = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
            AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
            else -> configuration.uiMode
        }
        return context.createConfigurationContext(configuration)
    }

    override fun getColor(colorRes: Int) = ContextCompat.getColor(getThemedContext(), colorRes)

    override fun getAttrColor(colorRes: Int) =
        TypedValue().also { context.theme.resolveAttribute(colorRes, it, true) }.data
}