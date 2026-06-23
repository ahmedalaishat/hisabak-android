package com.hisabak.core.domain

import com.hisabak.core.domain.backup.AutoBackupPeriod
import kotlinx.coroutines.flow.Flow

/** Lightweight on-device app preferences (DataStore-backed). */
interface AppPreferences {
    /** Whether the user has finished the first-launch onboarding. */
    val onboardingCompleted: Flow<Boolean>

    suspend fun setOnboardingCompleted(value: Boolean)

    /** The chosen appearance; defaults to [ThemeMode.SYSTEM]. */
    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(value: ThemeMode)

    /** Whether the biometric/device-credential app lock is on; defaults to `false`. */
    val appLockEnabled: Flow<Boolean>

    suspend fun setAppLockEnabled(value: Boolean)

    /** Whether Google Drive backup is enabled; defaults to `false`. */
    val backupEnabled: Flow<Boolean>

    suspend fun setBackupEnabled(value: Boolean)

    /** Whether backups are encrypted with a passphrase; defaults to `true`. */
    val backupEncryptionEnabled: Flow<Boolean>

    suspend fun setBackupEncryptionEnabled(value: Boolean)

    /** How often automatic backups should run; defaults to [AutoBackupPeriod.DEFAULT]. */
    val autoBackupPeriod: Flow<AutoBackupPeriod>

    suspend fun setAutoBackupPeriod(value: AutoBackupPeriod)
}
