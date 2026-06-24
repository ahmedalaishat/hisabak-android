package com.hisabak.feature.settings.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hisabak.R
import com.hisabak.core.domain.ThemeMode
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SegmentOption
import com.hisabak.ui.components.SegmentedControl
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.Spacing

const val LANGUAGE_ENGLISH = "en"
const val LANGUAGE_ARABIC = "ar"

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    language: String,
    appLockEnabled: Boolean,
    appLockSupported: Boolean,
    onThemeChange: (ThemeMode) -> Unit,
    onLanguageChange: (String) -> Unit,
    onAppLockChange: (Boolean) -> Unit,
    onOpenBackup: () -> Unit,
    passphraseReminderVisible: Boolean,
    onConfirmRemembered: () -> Unit,
    onVerifyPassphrase: (String, (Boolean) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showVerify by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.sectionGap),
    ) {
        if (passphraseReminderVisible) {
            PassphraseReminderCard(onConfirm = onConfirmRemembered, onCheck = { showVerify = true })
        }

        SettingGroup(title = stringResource(R.string.settings_appearance)) {
            Text(
                stringResource(R.string.settings_appearance_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            SegmentedControl(
                options = listOf(
                    SegmentOption(ThemeMode.SYSTEM, stringResource(R.string.settings_theme_system)),
                    SegmentOption(ThemeMode.LIGHT, stringResource(R.string.settings_theme_light)),
                    SegmentOption(ThemeMode.DARK, stringResource(R.string.settings_theme_dark)),
                ),
                selected = themeMode,
                onSelect = onThemeChange,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s4),
            )
        }

        SettingGroup(title = stringResource(R.string.settings_language)) {
            Text(
                stringResource(R.string.settings_language_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            SegmentedControl(
                options = listOf(
                    SegmentOption(LANGUAGE_ENGLISH, stringResource(R.string.settings_language_english)),
                    SegmentOption(LANGUAGE_ARABIC, stringResource(R.string.settings_language_arabic)),
                ),
                selected = language,
                onSelect = onLanguageChange,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s4),
            )
        }

        SettingGroup(title = stringResource(R.string.settings_security)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.settings_app_lock),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        stringResource(R.string.settings_app_lock_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = appLockEnabled,
                    onCheckedChange = onAppLockChange,
                    enabled = appLockSupported,
                )
            }
        }

        SettingGroup(title = stringResource(R.string.settings_data)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenBackup),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.settings_backup_restore),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        stringResource(R.string.settings_backup_restore_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    if (showVerify) {
        PassphraseVerifySheet(
            onVerify = onVerifyPassphrase,
            onChangePassphrase = {
                showVerify = false
                onOpenBackup()
            },
            onDismiss = { showVerify = false },
        )
    }
}

@Composable
private fun PassphraseReminderCard(onConfirm: () -> Unit, onCheck: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), contentPadding = 0.dp) {
        Column(modifier = Modifier.padding(Spacing.cardPadding), verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                Box(
                    modifier = Modifier.size(26.dp).clip(CircleShape).background(MaterialTheme.colorScheme.error),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.PriorityHigh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    stringResource(R.string.settings_pass_reminder_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                stringResource(R.string.settings_pass_reminder_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        ReminderAction(stringResource(R.string.settings_pass_reminder_yes), onConfirm)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        ReminderAction(stringResource(R.string.settings_pass_reminder_check), onCheck)
    }
}

@Composable
private fun ReminderAction(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.cardPadding, vertical = Spacing.s4),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PassphraseVerifySheet(
    onVerify: (String, (Boolean) -> Unit) -> Unit,
    onChangePassphrase: () -> Unit,
    onDismiss: () -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }
    var wrong by rememberSaveable { mutableStateOf(false) }
    var busy by rememberSaveable { mutableStateOf(false) }
    var success by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.pageMargin)
                .padding(bottom = Spacing.sectionGap),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s4),
        ) {
            if (success) {
                SuccessCheck()
                Text(
                    stringResource(R.string.settings_pass_verify_success_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    stringResource(R.string.settings_pass_verify_success_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                HisabakButton(
                    text = stringResource(R.string.settings_pass_verify_done),
                    onClick = onDismiss,
                    fullWidth = true,
                )
            } else {
                Text(
                    stringResource(R.string.settings_pass_verify_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    stringResource(R.string.settings_pass_verify_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it; wrong = false },
                    label = { Text(stringResource(R.string.backup_passphrase)) },
                    singleLine = true,
                    isError = wrong,
                    supportingText = if (wrong) {
                        { Text(stringResource(R.string.settings_pass_verify_wrong)) }
                    } else {
                        null
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                )
                HisabakButton(
                    text = stringResource(R.string.settings_pass_verify_action),
                    onClick = {
                        busy = true
                        onVerify(input) { ok ->
                            busy = false
                            if (ok) success = true else wrong = true
                        }
                    },
                    enabled = input.isNotEmpty() && !busy,
                    fullWidth = true,
                )
                if (wrong) {
                    Text(
                        text = stringResource(R.string.settings_pass_change),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onChangePassphrase)
                            .padding(Spacing.s3),
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessCheck() {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val scale by animateFloatAsState(
        targetValue = if (shown) 1f else 0.3f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "check",
    )
    Box(
        modifier = Modifier
            .padding(top = Spacing.s4)
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Rounded.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(40.dp).graphicsLayer { scaleX = scale; scaleY = scale },
        )
    }
}

@Composable
private fun SettingGroup(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
        SectionHeader(title = title)
        SurfaceCard(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}
