package com.example.kukutrainer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CharacterSelectScreen(onSelect: (Int) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(id = R.string.select_character_prompt), style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(1) }) {
                Text(text = stringResource(id = R.string.character_1))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(2) }) {
                Text(text = stringResource(id = R.string.character_2))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(3) }) {
                Text(text = stringResource(id = R.string.character_3))
            }
        }
    }
}