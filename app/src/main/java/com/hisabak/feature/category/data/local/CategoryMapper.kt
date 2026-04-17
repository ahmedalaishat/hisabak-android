package com.hisabak.feature.category.data.local

import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import java.time.Instant

fun CategoryEntity.toDomain(): Category = Category(
    id = CategoryId(id),
    name = name,
    type = CategoryType.valueOf(type),
    color = color,
    icon = icon,
    sync = SyncMetadata(
        updatedAt = Instant.ofEpochMilli(updatedAtMillis),
        isDirty = isDirty,
        deletedAt = deletedAtMillis?.let(Instant::ofEpochMilli),
        serverId = serverId,
        version = version,
    ),
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id.value,
    name = name,
    type = type.name,
    color = color,
    icon = icon,
    updatedAtMillis = sync.updatedAt.toEpochMilli(),
    isDirty = sync.isDirty,
    deletedAtMillis = sync.deletedAt?.toEpochMilli(),
    serverId = sync.serverId,
    version = sync.version,
)
