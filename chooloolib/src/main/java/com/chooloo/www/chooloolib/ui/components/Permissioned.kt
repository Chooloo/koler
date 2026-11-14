package com.chooloo.www.chooloolib.ui.components

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Permissioned(
    permissions: List<String>,
    @StringRes rationaleRes: Int,
    optionalPermissions: List<String> = emptyList(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    fun missingPermissions() = permissions.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }

    var missing by remember(permissions) { mutableStateOf(missingPermissions()) }
    var isPermanentlyDenied by rememberSaveable { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            missing = missingPermissions()
            isPermanentlyDenied = activity != null && missing.isNotEmpty() &&
                missing.none { permission ->
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                }
        }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { missing = missingPermissions() }

    if (missing.isEmpty()) return content()

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.ExtraLarge),
    ) {
        Text(
            text = stringResource(rationaleRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = {
                if (isPermanentlyDenied) {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null),
                        ),
                    )
                } else {
                    launcher.launch((missing + optionalPermissions).toTypedArray())
                }
            },
        ) {
            Text(
                stringResource(
                    if (isPermanentlyDenied) {
                        R.string.action_open_settings
                    } else {
                        R.string.action_grant_access
                    },
                ),
            )
        }
    }
}
