package com.chooloo.www.chooloolib.domain.repository.navigation

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRepositoryImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    @ApplicationContext private val context: Context
) : BaseRepositoryImpl(), NavigationRepository {
    override suspend fun goToDonate() {
        context.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(context.getString(R.string.donation_link))
            ).addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToRateApp() {
        context.startActivity(
            Intent(ACTION_VIEW)
                .setData(Uri.parse("market://details?id=" + context.packageName))
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToSendEmail() {
        context.startActivity(
            Intent(ACTION_SEND)
                .setType("message/rfc822")
                .putExtra(EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToReportBug() {
        context.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(context.getString(R.string.app_bug_reporting_url))
            ).addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToAppGithub() {
        context.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(context.getString(R.string.app_source_url))
            ).addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToLauncherActivity() {
        context.startActivity(
            context.packageManager.getLaunchIntentForPackage(context.packageName)
                ?.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override suspend fun goToManageBlockedNumbers() {
        context.startActivity(
            telecomManager.createManageBlockedNumbersIntent()
                .addFlags(FLAG_ACTIVITY_NEW_TASK),
            null
        )
    }

    override suspend fun goToSendSms(number: String?) {
        context.startActivity(
            Intent(
                ACTION_SENDTO,
                Uri.parse("smsto:${PhoneNumberUtils.normalizeNumber(number)}")
            ).addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToWhatsappNumber(number: String?) {
        context.startActivity(
            Intent(ACTION_VIEW)
                .setData(Uri.parse("http://api.whatsapp.com/send?phone=$number"))
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun goToActivity(activityClass: Class<out BaseActivity>) {
        context.startActivity(
            Intent(context, activityClass)
                .addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}