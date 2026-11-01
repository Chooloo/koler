package com.chooloo.www.chooloolib.ui.common

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.chooloo.www.chooloolib.domain.repository.permission.PermissionRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DefaultDialerRequestActivity : BaseActivity() {
    @Inject lateinit var permissionRepository: PermissionRepository

    private val defaultDialerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        lifecycleScope.launch {
            permissionRepository.entryDefaultDialerResult(result.resultCode == RESULT_OK)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            checkDefaultDialer()
        }
    }

    override fun onSetup() {}

    private fun checkDefaultDialer() {
        if (permissionRepository.isDefaultDialer) {
            lifecycleScope.launch {
                permissionRepository.entryDefaultDialerResult(true)
                finish()
            }
            return
        }

        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (getSystemService(Context.ROLE_SERVICE) as RoleManager).createRequestRoleIntent(
                RoleManager.ROLE_DIALER
            )
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName
            )
        }
        defaultDialerLauncher.launch(intent)
    }
}
