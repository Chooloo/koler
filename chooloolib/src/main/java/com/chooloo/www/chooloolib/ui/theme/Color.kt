package com.chooloo.www.chooloolib.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4B635C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCDE7ED),
    onPrimaryContainer = Color(0xFF07201A),
    secondary = Color(0xFF4B635C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDE7ED),
    onSecondaryContainer = Color(0xFF07201A),
    tertiary = Color(0xFF545D7E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC7E7FF),
    onTertiaryContainer = Color(0xFF001E2E),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBFCFD),
    onBackground = Color(0xFF191C1D),
    surface = Color(0xFFFBFCFD),
    onSurface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFFDBE4E6),
    onSurfaceVariant = Color(0xFF3F484A),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5BDBBE),
    onPrimary = Color(0xFF00382E),
    primaryContainer = Color(0xFF005143),
    onPrimaryContainer = Color(0xFF7AF8DA),
    secondary = Color(0xFFB1CCC3),
    onSecondary = Color(0xFF00382E),
    secondaryContainer = Color(0xFF334B45),
    onSecondaryContainer = Color(0xFFCDE8DF),
    tertiary = Color(0xFFAACBE3),
    onTertiary = Color(0xFF103447),
    tertiaryContainer = Color(0xFF2A4A5F),
    onTertiaryContainer = Color(0xFFC7E7FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1B),
    onBackground = Color(0xFFE1E3E0),
    surface = Color(0xFF191C1B),
    onSurface = Color(0xFFE1E3E0),
    surfaceVariant = Color(0xFF3F4945),
    onSurfaceVariant = Color(0xFFBFC9C4),
)

@Immutable
data class ExtendedColors(
    val positive: Color,
    val negative: Color,
)

val LightExtendedColors = ExtendedColors(
    positive = Color(0xFF7BD46B),
    negative = Color(0xFFFF6961),
)

val DarkExtendedColors = ExtendedColors(
    positive = Color(0xFF378A28),
    negative = Color(0xFFD40A00),
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }
