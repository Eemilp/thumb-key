package com.dessalines.thumbkey.ui.components.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dessalines.thumbkey.db.AppSettings
import com.dessalines.thumbkey.db.DEFAULT_ANIMATION_HELPER_SPEED
import com.dessalines.thumbkey.db.DEFAULT_ANIMATION_SPEED
import com.dessalines.thumbkey.db.DEFAULT_AUTO_CAPITALIZE
import com.dessalines.thumbkey.db.DEFAULT_KEYBOARD_LAYOUT
import com.dessalines.thumbkey.db.DEFAULT_KEY_SIZE
import com.dessalines.thumbkey.db.DEFAULT_MIN_SWIPE_LENGTH
import com.dessalines.thumbkey.db.DEFAULT_POSITION
import com.dessalines.thumbkey.db.DEFAULT_PUSHUP_SIZE
import com.dessalines.thumbkey.db.DEFAULT_SOUND_ON_TAP
import com.dessalines.thumbkey.db.DEFAULT_VIBRATE_ON_TAP
import com.dessalines.thumbkey.keyboards.THUMBKEY_EN_V4_MAIN
import com.dessalines.thumbkey.utils.KeyAction
import com.dessalines.thumbkey.utils.KeyboardLayout
import com.dessalines.thumbkey.utils.KeyboardMode
import com.dessalines.thumbkey.utils.KeyboardPosition
import com.dessalines.thumbkey.utils.keyboardLayoutToModes
import com.dessalines.thumbkey.utils.keyboardPositionToAlignment
import com.dessalines.thumbkey.utils.toBool

@Composable
fun KeyboardScreen(
    settings: AppSettings?,
    startMode: KeyboardMode
) {
    var mode by remember {
        mutableStateOf(startMode)
    }

    // TODO get rid of this crap
    val lastAction = remember { mutableStateOf<KeyAction?>(null) }

    val keyboardGroup = keyboardLayoutToModes(
        KeyboardLayout.values()[
            settings?.keyboardLayout
                ?: DEFAULT_KEYBOARD_LAYOUT
        ]
    )

    val keyboard = keyboardGroup[mode] ?: THUMBKEY_EN_V4_MAIN

    val alignment = keyboardPositionToAlignment(
        KeyboardPosition.values()[
            settings?.position
                ?: DEFAULT_POSITION
        ]
    )
    val pushupSizeDp = (settings?.pushupSize ?: DEFAULT_PUSHUP_SIZE).dp

    val autoCapitalize = (settings?.autoCapitalize ?: DEFAULT_AUTO_CAPITALIZE).toBool()
    val vibrateOnTap = (settings?.vibrateOnTap ?: DEFAULT_VIBRATE_ON_TAP).toBool()
    val soundOnTap = (settings?.soundOnTap ?: DEFAULT_SOUND_ON_TAP).toBool()

    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .padding(bottom = pushupSizeDp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            keyboard.arr.forEach { row ->
                Row {
                    row.forEach { key ->
                        Column {
                            KeyboardKey(
                                key = key,
                                lastAction = lastAction,
                                keySize = settings?.keySize ?: DEFAULT_KEY_SIZE,
                                autoCapitalize = autoCapitalize,
                                vibrateOnTap = vibrateOnTap,
                                soundOnTap = soundOnTap,
                                animationSpeed = settings?.animationSpeed
                                    ?: DEFAULT_ANIMATION_SPEED,
                                animationHelperSpeed = settings?.animationHelperSpeed
                                    ?: DEFAULT_ANIMATION_HELPER_SPEED,
                                minSwipeLength = settings?.minSwipeLength ?: DEFAULT_MIN_SWIPE_LENGTH,
                                onToggleShiftMode = { enable ->
                                    if (mode !== KeyboardMode.NUMERIC) {
                                        mode = if (enable) {
                                            KeyboardMode.SHIFTED
                                        } else {
                                            KeyboardMode.MAIN
                                        }
                                    }
                                },
                                onToggleNumericMode = { numeric ->
                                    mode = if (numeric) {
                                        KeyboardMode.NUMERIC
                                    } else {
                                        KeyboardMode.MAIN
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}