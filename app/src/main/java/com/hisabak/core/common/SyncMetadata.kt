package com.hisabak.core.common

import java.time.Instant

data class SyncMetadata(
    val updatedAt: Instant,
    val isDirty: Boolean = true,
    val deletedAt: Instant? = null,
    val serverId: String? = null,
    val version: Long = 0L,
) {
    val isDeleted: Boolean get() = deletedAt != null
}
