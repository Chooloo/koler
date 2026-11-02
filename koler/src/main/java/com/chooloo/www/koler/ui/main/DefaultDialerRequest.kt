package com.chooloo.www.koler.ui.main

import android.app.role.RoleManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun DefaultDialerRequest() {
    val context = LocalContext.current
    val roleManager = context.getSystemService(RoleManager::class.java)
    var isRequested by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    LaunchedEffect(Unit) {
        if (!isRequested && roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
            isRequested = true
            launcher.launch(roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER))
        }
    }
}
