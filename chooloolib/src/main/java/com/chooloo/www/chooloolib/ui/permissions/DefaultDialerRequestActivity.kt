package com.chooloo.www.chooloolib.ui.permissions

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import androidx.activity.viewModels
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DefaultDialerRequestActivity : BaseActivity<BaseViewState>() {
    override val contentView: View? = null
    override val viewState: BaseViewState by viewModels()

    @Inject lateinit var permissions: PermissionsInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            checkDefaultDialer()
        }
    }

    override fun _onSetup() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DEFAULT_DIALER) {
            when (resultCode) {
                RESULT_OK -> permissions.entryDefaultDialerResult(true)
                else -> permissions.entryDefaultDialerResult(false)
            }
        }
        finish()
    }


    private fun checkDefaultDialer() {
        if (permissions.isDefaultDialer) {
            permissions.entryDefaultDialerResult(true)
            finish()
        }

        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (getSystemService(Context.ROLE_SERVICE) as RoleManager).createRequestRoleIntent(
                RoleManager.ROLE_DIALER
            )
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                packageName
            )
        }
        startActivityForResult(intent, REQUEST_CODE_DEFAULT_DIALER)
    }

    companion object {
        const val REQUEST_CODE_DEFAULT_DIALER = 1
    }
}