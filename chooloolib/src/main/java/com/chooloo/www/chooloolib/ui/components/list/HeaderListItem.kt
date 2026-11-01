package com.chooloo.www.chooloolib.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun HeaderListItem(
    modifier: Modifier = Modifier,
    header: String
) {
    Box(
        modifier = modifier
            .padding(
                end = Spacing.Medium,
                top = Spacing.Medium,
                bottom = Spacing.Small,
                start = Spacing.Small,
            )
            .clip(MaterialTheme.shapes.large)
            .background(colorScheme.surfaceContainerHigh)
    ) {
        Text(
            text = header,
            color = colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            style = typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall)
        )
    }
}

@Preview
@Composable
fun HeaderListItemPreview() {
    HeaderListItem(header = "2 Days Ago")
}
