package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissioned(
    permissions: List<String>,
    onIsGrantedChange: (isGranted: Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(key1 = permissionsState.allPermissionsGranted) {
        onIsGrantedChange(permissionsState.allPermissionsGranted)
    }

    if (permissionsState.allPermissionsGranted) {
        content()
    } else {
        val missingPermissions = permissionsState.permissions.filter { !it.status.isGranted }
        Column {
            if (missingPermissions.size == 1) {
                Text("I need ${missingPermissions[0].permission} permissions :)")
            } else {
                Text("I need to following permissions pls :)")
                missingPermissions.map {
                    Text(it.permission)
                }
            }

            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                if (missingPermissions.size == 1) {
                    Text("Request Permission")
                } else {
                    Text("Request Permissions")
                }
            }
        }
    }
}