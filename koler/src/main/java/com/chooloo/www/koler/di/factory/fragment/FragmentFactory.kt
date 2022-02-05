package com.chooloo.www.koler.di.factory.fragment

import com.chooloo.www.koler.ui.settings.SettingsFragment

interface FragmentFactory {
    fun getSettingsFragment(): SettingsFragment
}