package com.hisabak.feature.transaction.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query(
        """
        SELECT * FROM transactions
        WHERE deletedAtMillis IS NULL
          AND (:brandId IS NULL OR brandId = :brandId)
          AND (:dateFromMillis IS NULL OR occurredAtMillis >= :dateFromMillis)
          AND (:dateToMillis IS NULL OR occurredAtMillis <= :dateToMillis)
          AND (:search IS NULL OR note LIKE '%' || :search || '%' COLLATE NOCASE)
        ORDER BY occurredAtMillis DESC
        """,
    )
    fun observeFiltered(
        search: String?,
        brandId: String?,
        dateFromMillis: Long?,
        dateToMillis: Long?,
    ): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions
        WHERE deletedAtMillis IS NULL
          AND (:brandId IS NULL OR brandId = :brandId)
          AND (:dateFromMillis IS NULL OR occurredAtMillis >= :dateFromMillis)
          AND (:dateToMillis IS NULL OR occurredAtMillis <= :dateToMillis)
          AND (:search IS NULL OR note LIKE '%' || :search || '%' COLLATE NOCASE)
        ORDER BY occurredAtMillis DESC
        LIMIT :limit OFFSET :offset
        """,
    )
    suspend fun pageFiltered(
        search: String?,
        brandId: String?,
        dateFromMillis: Long?,
        dateToMillis: Long?,
        limit: Int,
        offset: Int,
    ): List<TransactionEntity>

    @Query(
        """
        SELECT COUNT(*) FROM transactions
        WHERE deletedAtMillis IS NULL
          AND (:brandId IS NULL OR brandId = :brandId)
          AND (:dateFromMillis IS NULL OR occurredAtMillis >= :dateFromMillis)
          AND (:dateToMillis IS NULL OR occurredAtMillis <= :dateToMillis)
          AND (:search IS NULL OR note LIKE '%' || :search || '%' COLLATE NOCASE)
        """,
    )
    suspend fun countFiltered(
        search: String?,
        brandId: String?,
        dateFromMillis: Long?,
        dateToMillis: Long?,
    ): Long

    @Query("SELECT * FROM transactions WHERE id = :id AND deletedAtMillis IS NULL")
    suspend fun getById(id: String): TransactionEntity?

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(entity: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TransactionEntity>)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: String)

    // Backup: every row incl. soft-deleted tombstones.
    @Query("SELECT * FROM transactions")
    suspend fun getAllForBackup(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM transactions WHERE brandId = :brandId AND deletedAtMillis IS NULL")
    suspend fun countForBrand(brandId: String): Long

    @Query(
        "UPDATE transactions SET brandId = :toBrandId, updatedAtMillis = :nowMillis, isDirty = 1 " +
            "WHERE brandId = :fromBrandId",
    )
    suspend fun reassignBrand(fromBrandId: String, toBrandId: String, nowMillis: Long)
}
