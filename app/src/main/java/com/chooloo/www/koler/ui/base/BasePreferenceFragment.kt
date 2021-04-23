package com.chooloo.www.koler.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import com.chooloo.www.koler.util.preferences.KolerPreferences

abstract class BasePreferenceFragment : PreferenceFragmentCompat(), BaseContract.View {
    abstract val preferenceResource: Int

    protected val kolerPreferences by lazy { context?.let { KolerPreferences(it) } }
    protected val _activity by lazy { context as BaseActivity }

    val argsSafely: Bundle
        get() = arguments ?: Bundle()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        _activity.onAttachFragment(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(preferenceResource, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        setDividerHeight(0)
        initAllPreferences()
        onSetup()
    }

    override fun onSetup() {}

    private fun initAllPreferences() {
        (0 until preferenceScreen.preferenceCount).forEach { x ->
            val preference = preferenceScreen.getPreference(x)
            if (preference is PreferenceGroup) {
                (0 until preference.preferenceCount).forEach { y ->
                    val nestedPreference = preference.getPreference(y)
                    if (nestedPreference is PreferenceGroup) {
                        (0 until nestedPreference.preferenceCount).forEach { k ->
                            initPreference(nestedPreference.getPreference(k))
                        }
                    } else {
                        initPreference(nestedPreference)
                    }
                }
            } else {
                initPreference(preference)
            }
        }
    }

    private fun initPreference(preference: Preference) {
        preference.apply {
            setOnPreferenceClickListener {
                onPreferenceClickListener(preference)
                true
            }
            setOnPreferenceChangeListener { preference, newValue ->
                onPreferenceChangeListener(preference, newValue)
                true
            }
        }
    }

    protected fun <T : Preference> getPreference(@StringRes keyString: Int) =
        findPreference<T>(getString(keyString))

    override fun hasPermission(permission: String) = true
    override fun hasPermissions(permissions: Array<String>) = true
    override fun showMessage(stringResId: Int) = showMessage(getString(stringResId))
    override fun showError(message: String) = showMessage(message)
    override fun showError(stringResId: Int) = showMessage(stringResId)
    override fun getColor(color: Int) = resources.getColor(color)
    override fun showMessage(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    open fun onPreferenceClickListener(preference: Preference) {}
    open fun onPreferenceChangeListener(preference: Preference, newValue: Any) {}
}