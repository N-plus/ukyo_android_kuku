package com.example.kukutrainer.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var bgmOn = remember { mutableStateOf(PreferencesManager.isBgmOn(context)) }
    var fast = remember { mutableStateOf(PreferencesManager.getSoundSpeed(context) >= 1f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.bgm), modifier = Modifier.weight(1f))
            Switch(checked = bgmOn.value, onCheckedChange = {
                bgmOn.value = it
                PreferencesManager.setBgmOn(context, it)
            })
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val label = if (fast.value) stringResource(id = R.string.sound_speed_fast) else stringResource(id = R.string.sound_speed_slow)
            Text(text = label, modifier = Modifier.weight(1f))
            Switch(checked = fast.value, onCheckedChange = {
                fast.value = it
                PreferencesManager.setSoundSpeed(context, if (it) 1f else 0.5f)
            })
        }

        Button(onClick = { navController.navigate(Screen.CharacterSelection.route) }) {
            Text(text = stringResource(id = R.string.change_character))
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.language_placeholder), modifier = Modifier.weight(1f))
            Switch(checked = false, onCheckedChange = {}, enabled = false)
        }
    }
}