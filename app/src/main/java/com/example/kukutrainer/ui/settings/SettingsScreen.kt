@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kukutrainer.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen

data class SettingsState(
    val bgmEnabled: Boolean = true,
    val voiceEnabled: Boolean = true,
    val selectedVoiceSpeed: Float = 1.0f,
    val soundCutEnabled: Boolean = false
)

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var settingsState by remember {
        mutableStateOf(
            SettingsState(
                bgmEnabled = PreferencesManager.isBgmOn(context),
                voiceEnabled = true,
                selectedVoiceSpeed = PreferencesManager.getSoundSpeed(context)
            )
        )
    }

    SettingsContent(
        settingsState = settingsState,
        onSettingsChanged = { state ->
            settingsState = state
            PreferencesManager.setBgmOn(context, state.bgmEnabled)
            PreferencesManager.setSoundSpeed(context, state.selectedVoiceSpeed)
        },
        onCharacterSelectionClicked = { navController.navigate(Screen.CharacterSelection.route) },
        onBackPressed = { navController.popBackStack() }
    )
}

@Composable
private fun SettingsContent(
    settingsState: SettingsState = SettingsState(),
    onSettingsChanged: (SettingsState) -> Unit = {},
    onCharacterSelectionClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    var currentSettings by remember { mutableStateOf(settingsState) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFF1F3F4)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            SettingsHeader(onBackPressed = onBackPressed)

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item { SettingsSectionTitle("音響設定") }

                item {
                    SwitchSettingItem(
                        title = "BGM",
                        description = "背景音楽のオン/オフ",
                        icon = Icons.Default.MusicNote,
                        checked = currentSettings.bgmEnabled,
                        onCheckedChange = { enabled ->
                            currentSettings = currentSettings.copy(bgmEnabled = enabled)
                            onSettingsChanged(currentSettings)
                        }
                    )
                }

                item {
                    SwitchSettingItem(
                        title = "音声読み上げ",
                        description = "文字の読み上げ機能",
                        icon = Icons.Default.RecordVoiceOver,
                        checked = currentSettings.voiceEnabled,
                        onCheckedChange = { enabled ->
                            currentSettings = currentSettings.copy(voiceEnabled = enabled)
                            onSettingsChanged(currentSettings)
                        }
                    )
                }

                item {
                    ActionSettingItem(
                        title = "キャラクターを再選択",
                        description = "学習を案内するキャラクターを変更",
                        icon = Icons.Default.Face,
                        onClick = onCharacterSelectionClicked
                    )
                }

                item { SettingsSectionTitle("その他") }

                item {
                    SwitchSettingItem(
                        title = "音声切替（準備中）",
                        description = "音声の種類を変更する機能",
                        icon = Icons.Default.Settings,
                        checked = currentSettings.soundCutEnabled,
                        enabled = false,
                        onCheckedChange = { enabled ->
                            currentSettings = currentSettings.copy(soundCutEnabled = enabled)
                            onSettingsChanged(currentSettings)
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
private fun SettingsHeader(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(48.dp)
                .background(color = Color.White, shape = CircleShape)
                .shadow(2.dp, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "戻る",
                tint = Color(0xFF37474F),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "設定",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6C63FF).copy(alpha = 0.1f),
                            Color(0xFF6C63FF).copy(alpha = 0.05f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color(0xFF6C63FF),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF37474F),
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    )
}

@Composable
private fun SwitchSettingItem(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (enabled) 4.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled) {
                isPressed = true
                onCheckedChange(!checked)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color.White else Color.White.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (enabled) {
                                listOf(
                                    Color(0xFF6C63FF).copy(alpha = 0.15f),
                                    Color(0xFF6C63FF).copy(alpha = 0.05f)
                                )
                            } else {
                                listOf(
                                    Color.Gray.copy(alpha = 0.1f),
                                    Color.Gray.copy(alpha = 0.05f)
                                )
                            }
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) Color(0xFF6C63FF) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) Color(0xFF37474F) else Color.Gray
                )

                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = if (enabled) Color(0xFF757575) else Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = if (enabled) onCheckedChange else null,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF6C63FF),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    disabledCheckedThumbColor = Color.White,
                    disabledCheckedTrackColor = Color.Gray.copy(alpha = 0.5f),
                    disabledUncheckedThumbColor = Color.White,
                    disabledUncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun ActionSettingItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.15f),
                                Color(0xFF4CAF50).copy(alpha = 0.05f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF37474F)
                )

                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(24.dp)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsContent()
    }
}

