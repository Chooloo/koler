package com.chooloo.www.callmanager.ui.settings

import android.os.Bundle
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.ActivitySettingsBinding
import com.chooloo.www.callmanager.ui.base.BaseActivity
import com.chooloo.www.callmanager.ui.settings_fragment.SettingsFragment

class SettingsActivity : BaseActivity(), SettingsMvpView {
    private lateinit var _binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SettingsFragment.newInstance(), TAG_FRAGMENT)
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        private const val TAG_FRAGMENT = "fragment"
    }
}