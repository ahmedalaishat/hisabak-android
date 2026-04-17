package com.hisabak.core.common

import kotlin.math.abs

data class Money(
    val amountMinor: Long,
    val currency: Currency,
) : Comparable<Money> {

    operator fun plus(other: Money): Money {
        requireSameCurrency(other)
        return copy(amountMinor = amountMinor + other.amountMinor)
    }

    operator fun minus(other: Money): Money {
        requireSameCurrency(other)
        return copy(amountMinor = amountMinor - other.amountMinor)
    }

    operator fun unaryMinus(): Money = copy(amountMinor = -amountMinor)

    operator fun times(scalar: Int): Money = copy(amountMinor = amountMinor * scalar)

    fun abs(): Money = copy(amountMinor = abs(amountMinor))

    val isZero: Boolean get() = amountMinor == 0L
    val isPositive: Boolean get() = amountMinor > 0L
    val isNegative: Boolean get() = amountMinor < 0L

    override fun compareTo(other: Money): Int {
        requireSameCurrency(other)
        return amountMinor.compareTo(other.amountMinor)
    }

    private fun requireSameCurrency(other: Money) {
        require(currency == other.currency) {
            "Currency mismatch: $currency vs ${other.currency}"
        }
    }

    companion object {
        fun zero(currency: Currency): Money = Money(0L, currency)

        fun ofMajor(amount: Double, currency: Currency): Money =
            Money((amount * 100).toLong(), currency)
    }
}
