package com.yourorg.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun KukuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = KukuColor.Primary,
            onPrimary = KukuColor.OnPrimary,
            background = KukuColor.Background,
            onBackground = KukuColor.OnBackground,
            surface = KukuColor.Surface
        ),
        typography = KukuTypography,
        content = content
    )
}