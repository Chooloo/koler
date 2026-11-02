package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import com.chooloo.www.chooloolib.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    visible: Boolean,
    skipPartiallyExpanded: Boolean = false,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    if (visible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            TransparentNavigationBar()
            Box(modifier = Modifier.padding(start = Spacing.Page, end = Spacing.Page, bottom = Spacing.Medium)) {
                content()
            }
        }
    }
}

@Composable
private fun TransparentNavigationBar() {
    // The sheet runs in its own window, which doesn't inherit the activity's navigation bar setup.
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
    SideEffect { window?.isNavigationBarContrastEnforced = false }
}
