package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.chooloo.www.chooloolib.ui.theme.Layout

@Composable
fun Avatar(name: String?, photoUri: String?, size: Dp, modifier: Modifier = Modifier) {
    val initial = name?.firstOrNull(Char::isLetter)?.uppercaseChar()
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        if (initial != null) {
            Text(
                text = initial.toString(),
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontSize = with(LocalDensity.current) {
                    (size * Layout.AVATAR_CONTENT_RATIO).toSp()
                },
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(size * Layout.AVATAR_CONTENT_RATIO),
            )
        }
        photoUri?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
