package com.hisabak.feature.metrics.domain

import com.hisabak.core.common.DateRange
import java.time.temporal.ChronoUnit

data class Period(
    val range: DateRange,
    val granularity: ChronoUnit = ChronoUnit.DAYS,
)
