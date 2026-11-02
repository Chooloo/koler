package com.chooloo.www.koler.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.components.FloatingPill
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.koler.ui.main.MainViewModel.Tab

@Composable
fun MainNavigationBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    onDialpadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingPill(modifier = modifier) {
        TabBubble(Icons.Rounded.History, stringResource(R.string.recents), selectedTab == Tab.RECENTS) {
            onTabSelected(Tab.RECENTS)
        }
        FloatingBubble(
            icon = Icons.Rounded.Dialpad,
            contentDescription = stringResource(R.string.cd_dialpad),
            onClick = onDialpadClick,
            size = Sizes.Button,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            hasShadow = false,
        )
        TabBubble(Icons.Rounded.Person, stringResource(R.string.contacts), selectedTab == Tab.CONTACTS) {
            onTabSelected(Tab.CONTACTS)
        }
    }
}

@Composable
private fun TabBubble(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    FloatingBubble(
        icon = icon,
        contentDescription = label,
        onClick = onClick,
        size = Sizes.Button,
        containerColor = animateColorAsState(
            if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
            label = "tabContainer",
        ).value,
        contentColor = animateColorAsState(
            if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            label = "tabContent",
        ).value,
        hasShadow = false,
    )
}
