package com.yourorg.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun KukuThemePreview() {
    KukuTheme {
        Text(
            text = "Kuku Theme",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(KukuSpacing.Medium)
        )
    }
}