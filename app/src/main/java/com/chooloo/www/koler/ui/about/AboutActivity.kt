package com.chooloo.www.koler.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.chooloo.www.koler.BuildConfig
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.ActivityAboutBinding
import com.chooloo.www.koler.ui.base.BaseActivity

class AboutActivity : BaseActivity(), AboutMvpView {
    private val _presenter: AboutMvpPresenter<AboutMvpView> by lazy { AboutPresenter() }
    private val _binding by lazy { ActivityAboutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.attach(this)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        _binding.apply {
            cardGeneral.aboutVersion.smallText = "" + BuildConfig.VERSION_NAME
            cardGeneral.aboutSource.setOnClickListener { _presenter.onOpenSourceClick() }
            cardDeveloper.aboutEmail.setOnClickListener { _presenter.onSendEmailClick() }
            cardSupport.aboutBugs.setOnClickListener { _presenter.onReportBugClick() }
            cardSupport.aboutRate.setOnClickListener { _presenter.onRateAppClick() }
            cardSupport.aboutDonate.setOnClickListener { _presenter.onDonateClick() }
        }
    }

    override fun openSource() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_source_url))))
    }

    override fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
        })
    }

    override fun reportBug() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.app_bug_reporting_url))
            )
        )
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