package com.example.kukutrainer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Represents the type of feedback animation to show.
 */
enum class FeedbackState {
    None,
    Correct,
    Incorrect,
    Completed
}

/**
 * Shows a simple animation depending on [state].
 * - [FeedbackState.Correct]: star jumps with scale effect.
 * - [FeedbackState.Incorrect]: shows [hint] text.
 * - [FeedbackState.Completed]: big star pops in.
 */
@Composable
fun FeedbackAnimation(state: FeedbackState, hint: String = "") {
    Box(contentAlignment = Alignment.Center) {
        when (state) {
            FeedbackState.Correct -> {
                val scale by animateFloatAsState(targetValue = 1.2f, label = "correctScale")
                val offset = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    offset.animateTo(
                        -30f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationY = offset.value
                        }
                )
            }

            FeedbackState.Incorrect -> {
                AnimatedVisibility(true) {
                    Text(text = hint, color = MaterialTheme.colorScheme.error)
                }
            }

            FeedbackState.Completed -> {
                val scale = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    scale.animateTo(
                        1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        }
                )
            }

            else -> Unit
        }
    }
}
