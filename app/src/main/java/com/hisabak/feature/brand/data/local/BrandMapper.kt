package com.hisabak.feature.brand.data.local

import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import java.time.Instant

fun BrandEntity.toDomain(): Brand = Brand(
    id = BrandId(id),
    name = name,
    categoryId = categoryId?.let(::CategoryId),
    sync = SyncMetadata(
        updatedAt = Instant.ofEpochMilli(updatedAtMillis),
        isDirty = isDirty,
        deletedAt = deletedAtMillis?.let(Instant::ofEpochMilli),
        serverId = serverId,
        version = version,
    ),
)

fun Brand.toEntity(): BrandEntity = BrandEntity(
    id = id.value,
    name = name,
    categoryId = categoryId?.value,
    updatedAtMillis = sync.updatedAt.toEpochMilli(),
    isDirty = sync.isDirty,
    deletedAtMillis = sync.deletedAt?.toEpochMilli(),
    serverId = sync.serverId,
    version = sync.version,
)
