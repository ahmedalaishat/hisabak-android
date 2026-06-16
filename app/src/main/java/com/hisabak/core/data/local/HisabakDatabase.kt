package com.hisabak.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hisabak.feature.brand.data.local.BrandDao
import com.hisabak.feature.brand.data.local.BrandEntity
import com.hisabak.feature.category.data.local.CategoryDao
import com.hisabak.feature.category.data.local.CategoryEntity
import com.hisabak.feature.category.data.local.CategoryLimitDao
import com.hisabak.feature.category.data.local.CategoryLimitEntity
import com.hisabak.feature.sms.data.local.SmsDao
import com.hisabak.feature.sms.data.local.SmsMessageEntity
import com.hisabak.feature.transaction.data.local.TransactionDao
import com.hisabak.feature.transaction.data.local.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        CategoryLimitEntity::class,
        BrandEntity::class,
        TransactionEntity::class,
        SmsMessageEntity::class,
    ],
    version = 1, // v1.0.0 baseline schema; add real migrations for future versions
    exportSchema = false,
)
abstract class HisabakDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryLimitDao(): CategoryLimitDao
    abstract fun brandDao(): BrandDao
    abstract fun transactionDao(): TransactionDao
    abstract fun smsDao(): SmsDao

    companion object {
        const val NAME = "hisabak.db"
    }
}
