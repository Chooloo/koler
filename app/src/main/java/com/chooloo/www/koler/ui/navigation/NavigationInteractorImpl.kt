package com.chooloo.www.koler.ui.navigation

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.telecom.TelecomManager
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.BaseObservable

class NavigationInteractorImpl(
    private val activity: BaseActivity,
    private val telecomManager: TelecomManager,
    private val stringInteractor: StringInteractor
) :
    BaseObservable<NavigationInteractor.Listener>(),
    NavigationInteractor {

    override fun goToRateApp() {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=" + activity.application.packageName)
        activity.startActivity(intent)
    }

    override fun goToSendEmail() {
        activity.startActivity(Intent(ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(EXTRA_EMAIL, arrayOf(stringInteractor.getString(R.string.support_email)))
        })
    }

    override fun goToAppGithub() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.app_source_url))
            )
        )
    }

    override fun goToReportBugPage() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.app_bug_reporting_url))
            )
        )
    }

    override fun goToDonatePage() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.donation_link))
            )
        )
    }

    override fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    override fun goToManageBlockedNumbers() {
        TODO("Not yet implemented")
    }
}