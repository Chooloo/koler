package com.chooloo.www.koler.di.factory.fragment

import com.chooloo.www.koler.ui.settings.SettingsFragment
import javax.inject.Inject

class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getSettingsFragment() = SettingsFragment()
}