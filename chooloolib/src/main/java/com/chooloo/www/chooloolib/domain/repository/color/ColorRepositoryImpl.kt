package com.chooloo.www.chooloolib.domain.repository.color

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseRepositoryImpl(), ColorRepository {
    private fun getThemedContext(): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.uiMode = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
            AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
            else -> configuration.uiMode
        }
        return context.createConfigurationContext(configuration)
    }

    override suspend fun getColor(colorRes: Int) =
        ContextCompat.getColor(getThemedContext(), colorRes)

    override suspend fun getAttrColor(colorRes: Int) =
        TypedValue().also { context.theme.resolveAttribute(colorRes, it, true) }.data
}