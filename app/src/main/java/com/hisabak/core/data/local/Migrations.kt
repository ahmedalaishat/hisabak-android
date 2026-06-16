package com.hisabak.core.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * v1 → v2: adds the notifications and category_limit_alerts tables for budget-limit alerts.
 * Purely additive, so existing data (categories, brands, transactions, limits) is preserved.
 * The DDL mirrors what Room generates for [com.hisabak.feature.notification.data.local]
 * entities; keep it in sync if those change.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `notifications` (" +
                "`id` TEXT NOT NULL, " +
                "`title` TEXT NOT NULL, " +
                "`message` TEXT NOT NULL, " +
                "`type` TEXT NOT NULL, " +
                "`categoryId` TEXT, " +
                "`createdAtMillis` INTEGER NOT NULL, " +
                "`isRead` INTEGER NOT NULL, " +
                "PRIMARY KEY(`id`))",
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_notifications_createdAtMillis` " +
                "ON `notifications` (`createdAtMillis`)",
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_notifications_isRead` " +
                "ON `notifications` (`isRead`)",
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `category_limit_alerts` (" +
                "`categoryId` TEXT NOT NULL, " +
                "`periodMonth` INTEGER NOT NULL, " +
                "`lastLevel` INTEGER NOT NULL, " +
                "PRIMARY KEY(`categoryId`, `periodMonth`))",
        )
    }
}
