package com.chooloo.www.chooloolib.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.chooloo.www.chooloolib.R

private val GoogleSansFontFamily = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.google_sans_bold, FontWeight.Bold),
    Font(R.font.google_sans_bolditalic, FontWeight.Bold, FontStyle.Italic),
)

private val DefaultTypography = Typography()

val ChoolooTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = GoogleSansFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = GoogleSansFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = GoogleSansFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = GoogleSansFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = GoogleSansFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = GoogleSansFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = GoogleSansFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = GoogleSansFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = GoogleSansFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = GoogleSansFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = GoogleSansFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = GoogleSansFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = GoogleSansFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = GoogleSansFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = GoogleSansFontFamily),
)
