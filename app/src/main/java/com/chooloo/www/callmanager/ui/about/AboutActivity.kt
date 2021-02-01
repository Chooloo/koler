package com.chooloo.www.callmanager.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.chooloo.www.callmanager.BuildConfig
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.ActivityAboutBinding
import com.chooloo.www.callmanager.ui.base.BaseActivity
import com.chooloo.www.callmanager.ui.dialog.ChangelogDialog

class AboutActivity : BaseActivity(), AboutMvpView {
    private lateinit var _presenter: AboutPresenter<AboutMvpView>
    private lateinit var _binding: ActivityAboutBinding

    companion object {
        private const val TAG_CHANGELOG_DIALOG = "changelog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = AboutPresenter()
        _presenter.attach(this)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        _binding.apply {
            cardGeneral.aboutVersion.setSmallText("" + BuildConfig.VERSION_NAME)
            cardGeneral.aboutChangelog.setOnClickListener { view: View? -> _presenter.onChangelogClick() }
            cardGeneral.aboutSource.setOnClickListener { view: View? -> _presenter.onOpenSourceClick() }
            cardDeveloper.aboutEmail.setOnClickListener { view: View? -> _presenter.onSendEmailClick() }
            cardSupport.aboutBugs.setOnClickListener { view: View? -> _presenter.onReportBugClick() }
            cardSupport.aboutRate.setOnClickListener { view: View? -> _presenter.onRateAppClick() }
            cardSupport.aboutDonate.setOnClickListener { view: View? -> _presenter.onDonateClick() }
        }
    }

    override fun openChangelog() {
        ChangelogDialog().show(supportFragmentManager, TAG_CHANGELOG_DIALOG)
    }

    override fun openSource() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_source_url))))
    }

    override fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            type = "message/rfc822"
        })
    }

    override fun reportBug() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_bug_reporting_url))))
    }

    override fun rateApp() {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=" + application.packageName)
        })
    }

    override fun donate() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donation_link))))
    }
}