package com.hisabak.feature.category.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val color: String,
    val icon: String,
    val updatedAtMillis: Long,
    val isDirty: Boolean,
    val deletedAtMillis: Long?,
    val serverId: String?,
    val version: Long,
)
