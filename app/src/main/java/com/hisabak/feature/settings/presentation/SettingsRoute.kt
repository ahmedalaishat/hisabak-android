package com.hisabak.feature.settings.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.core.data.preferences.AppLocale
import com.hisabak.core.domain.ThemeMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
    // The effective UI language follows the current configuration, so the selection reflects
    // what's actually on screen after a locale switch recreates the activity.
    val language = if (LocalConfiguration.current.locales[0].language == LANGUAGE_ARABIC) {
        LANGUAGE_ARABIC
    } else {
        LANGUAGE_ENGLISH
    }

    SettingsScreen(
        themeMode = themeMode,
        language = language,
        onThemeChange = viewModel::setThemeMode,
        onLanguageChange = { tag ->
            if (tag != language) {
                viewModel.onLanguageSelected(tag)
                AppLocale.setLanguageTag(context, tag)
                (context as? Activity)?.recreate()
            }
        },
        modifier = modifier,
    )
}
