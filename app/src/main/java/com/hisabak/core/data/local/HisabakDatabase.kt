package com.hisabak.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hisabak.feature.brand.data.local.BrandDao
import com.hisabak.feature.brand.data.local.BrandEntity
import com.hisabak.feature.category.data.local.CategoryDao
import com.hisabak.feature.category.data.local.CategoryEntity
import com.hisabak.feature.category.data.local.CategoryLimitDao
import com.hisabak.feature.category.data.local.CategoryLimitEntity
import com.hisabak.feature.notification.data.local.CategoryLimitAlertDao
import com.hisabak.feature.notification.data.local.CategoryLimitAlertEntity
import com.hisabak.feature.notification.data.local.NotificationDao
import com.hisabak.feature.notification.data.local.NotificationEntity
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
        NotificationEntity::class,
        CategoryLimitAlertEntity::class,
    ],
    version = 2, // v2: notifications + category_limit_alerts
    exportSchema = true,
)
abstract class HisabakDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryLimitDao(): CategoryLimitDao
    abstract fun brandDao(): BrandDao
    abstract fun transactionDao(): TransactionDao
    abstract fun smsDao(): SmsDao
    abstract fun notificationDao(): NotificationDao
    abstract fun categoryLimitAlertDao(): CategoryLimitAlertDao

    companion object {
        const val NAME = "hisabak.db"
    }
}
