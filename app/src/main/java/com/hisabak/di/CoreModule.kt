package com.hisabak.di

import com.hisabak.core.common.Clock
import com.hisabak.core.common.Currency
import com.hisabak.core.common.SystemClock
import org.koin.dsl.module

val coreModule = module {
    single<Clock> { SystemClock() }
    single { Currency.AED }
    single { SeedData(clock = get(), currency = get()) }
}
