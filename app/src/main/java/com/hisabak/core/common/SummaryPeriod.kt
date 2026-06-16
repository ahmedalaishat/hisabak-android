package com.hisabak.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** A selectable reporting window shared by the dashboard summary cards. */
enum class SummaryPeriod(val label: String) {
    CURRENT_MONTH("This month"),
    LAST_MONTH("Last month"),
    CURRENT_YEAR("This year"),
    LAST_YEAR("Last year"),
    ALL("All time");

    /** [start, end) dates for this period relative to [today], or null for [ALL]. */
    fun dateRange(today: LocalDate): Pair<LocalDate, LocalDate>? = when (this) {
        CURRENT_MONTH -> today.withDayOfMonth(1).let { it to it.plusMonths(1) }
        LAST_MONTH -> today.withDayOfMonth(1).minusMonths(1).let { it to it.plusMonths(1) }
        CURRENT_YEAR -> today.withDayOfYear(1).let { it to it.plusYears(1) }
        LAST_YEAR -> today.withDayOfYear(1).minusYears(1).let { it to it.plusYears(1) }
        ALL -> null
    }

    /** [start, end) instant range relative to [today] in [zone], or null for [ALL]. */
    fun instantRange(today: LocalDate, zone: ZoneId): Pair<Instant, Instant>? =
        dateRange(today)?.let { (start, end) ->
            start.atStartOfDay(zone).toInstant() to end.atStartOfDay(zone).toInstant()
        }

    /** The equal-length window immediately before this one, for trend comparison. */
    fun previousInstantRange(today: LocalDate, zone: ZoneId): Pair<Instant, Instant>? {
        val previous = when (this) {
            CURRENT_MONTH, LAST_MONTH -> dateRange(today)?.let { (start, _) -> start.minusMonths(1) to start }
            CURRENT_YEAR, LAST_YEAR -> dateRange(today)?.let { (start, _) -> start.minusYears(1) to start }
            ALL -> null
        } ?: return null
        return previous.first.atStartOfDay(zone).toInstant() to previous.second.atStartOfDay(zone).toInstant()
    }
}
