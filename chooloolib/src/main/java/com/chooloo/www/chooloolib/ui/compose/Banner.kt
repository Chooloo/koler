package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.chooloo.www.chooloolib.R

@Composable
fun Banner(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Start,
        style = typography.titleMedium,
        modifier = Modifier
            .alpha(0.5f)
            .fillMaxWidth()
            .background(colorScheme.surfaceVariant)
            .padding(dimensionResource(R.dimen.spacing))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius)))
    )
}