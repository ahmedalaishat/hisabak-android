package com.hisabak.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hisabak.R
import com.hisabak.core.domain.ThemeMode
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.sectionGap),
    ) {
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
