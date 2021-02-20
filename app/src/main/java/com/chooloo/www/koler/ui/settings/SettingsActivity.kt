package com.chooloo.www.koler.ui.settings

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.ActivitySettingsBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.settingsfragment.SettingsFragment

class SettingsActivity : BaseActivity(), SettingsMvpView {
    private val _binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, SettingsFragment.newInstance(), TAG_FRAGMENT).commitNow()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        private const val TAG_FRAGMENT = "fragment"
    }
}