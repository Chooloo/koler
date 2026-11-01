package com.chooloo.www.chooloolib.ui.components

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Permissioned(
    permissions: List<String>,
    onIsGrantedChange: (isGranted: Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    fun getMissingPermissions(): List<String> =
        permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

    var missingPermissions by remember(permissions) {
        mutableStateOf(getMissingPermissions())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        missingPermissions = getMissingPermissions()
    }

    DisposableEffect(lifecycleOwner, permissions) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                missingPermissions = getMissingPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val allGranted = missingPermissions.isEmpty()

    LaunchedEffect(key1 = allGranted) {
        onIsGrantedChange(allGranted)
    }

    if (allGranted) {
        content()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.ExtraLarge),
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Medium,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (missingPermissions.size == 1) {
                Text(
                    text = stringResource(
                        R.string.permission_request_single,
                        missingPermissions[0].substringAfterLast('.')
                    ),
                    style = typography.bodyLarge
                )
            } else {
                Text(
                    text = stringResource(R.string.permission_request_multiple),
                    style = typography.bodyLarge
                )
                missingPermissions.forEach { perm ->
                    Text(
                        text = perm.substringAfterLast('.'),
                        style = typography.bodyMedium
                    )
                }
            }

            Button(onClick = { launcher.launch(missingPermissions.toTypedArray()) }) {
                Text(
                    stringResource(
                        if (missingPermissions.size == 1) {
                            R.string.action_request_permission
                        } else {
                            R.string.action_request_permissions
                        }
                    )
                )
            }
        }
    }
}
