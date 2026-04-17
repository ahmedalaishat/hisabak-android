package com.hisabak.feature.brand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    @Query(
        """
        SELECT * FROM brands
        WHERE deletedAtMillis IS NULL
          AND (:search IS NULL OR name LIKE '%' || :search || '%' COLLATE NOCASE)
          AND (:categoryId IS NULL OR categoryId = :categoryId)
        ORDER BY LOWER(name)
        """,
    )
    fun observeFiltered(search: String?, categoryId: String?): Flow<List<BrandEntity>>

    @Query("SELECT * FROM brands WHERE id = :id AND deletedAtMillis IS NULL")
    suspend fun getById(id: String): BrandEntity?

    @Query(
        """
        SELECT * FROM brands
        WHERE deletedAtMillis IS NULL
          AND (name LIKE '%' || :name || '%' COLLATE NOCASE
               OR :name LIKE '%' || name || '%' COLLATE NOCASE)
        LIMIT 1
        """,
    )
    suspend fun findByNameLike(name: String): BrandEntity?

    @Query("SELECT COUNT(*) FROM brands")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: BrandEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<BrandEntity>)

    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteById(id: String)
}
