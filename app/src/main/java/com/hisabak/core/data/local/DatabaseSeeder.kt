package com.hisabak.core.data.local

import androidx.room.withTransaction
import com.hisabak.di.SeedData
import com.hisabak.feature.brand.data.local.toEntity
import com.hisabak.feature.category.data.local.toEntity
import com.hisabak.feature.transaction.data.local.toEntity

class DatabaseSeeder(
    private val db: HisabakDatabase,
    private val seed: SeedData,
) {
    suspend fun seedIfEmpty() {
        if (db.categoryDao().count() > 0) return
        db.withTransaction {
            db.categoryDao().upsertAll(seed.categories.map { it.toEntity() })
            db.brandDao().upsertAll(seed.brands.map { it.toEntity() })
            db.transactionDao().upsertAll(seed.transactions.map { it.toEntity() })
        }
    }
}
