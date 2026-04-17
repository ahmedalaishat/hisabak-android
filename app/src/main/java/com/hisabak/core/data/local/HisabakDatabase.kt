package com.hisabak.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hisabak.feature.brand.data.local.BrandDao
import com.hisabak.feature.brand.data.local.BrandEntity
import com.hisabak.feature.category.data.local.CategoryDao
import com.hisabak.feature.category.data.local.CategoryEntity
import com.hisabak.feature.transaction.data.local.TransactionDao
import com.hisabak.feature.transaction.data.local.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        BrandEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class HisabakDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun brandDao(): BrandDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val NAME = "hisabak.db"
    }
}
