package com.hisabak.di

import com.hisabak.feature.brand.brandModule
import com.hisabak.feature.category.categoryModule
import com.hisabak.feature.dashboard.dashboardModule
import com.hisabak.feature.transaction.transactionModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
    coreModule,
    databaseModule,
    categoryModule,
    brandModule,
    transactionModule,
    dashboardModule,
)
