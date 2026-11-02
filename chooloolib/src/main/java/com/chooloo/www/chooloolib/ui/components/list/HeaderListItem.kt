package com.chooloo.www.chooloolib.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun HeaderListItem(
    modifier: Modifier = Modifier,
    header: String
) {
    Box(
        modifier = modifier
            .padding(
                start = Spacing.Small,
                top = Spacing.Medium,
                bottom = Spacing.Small,
            )
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Text(
            text = header,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall)
        )
    }
}
