package com.hisabak.core.domain.backup

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * Schedules (or cancels) recurring background backups. Domain-level so the policy stays platform-free
 * (Android uses WorkManager today; another platform plugs in its own scheduler behind this).
 */
interface AutoBackupScheduler {
    /** Reconcile the schedule with the current settings: cancel when disabled / [AutoBackupPeriod.NEVER]. */
    fun schedule(period: AutoBackupPeriod, enabled: Boolean)
}

/** The repeat interval for a period, or null when no recurring backup should run. Pure + testable. */
fun autoBackupInterval(period: AutoBackupPeriod): Duration? = when (period) {
    AutoBackupPeriod.NEVER -> null
    AutoBackupPeriod.DAILY -> 1.days
    AutoBackupPeriod.WEEKLY -> 7.days
    AutoBackupPeriod.MONTHLY -> 30.days
}
