package com.hisabak.core.domain.backup

import kotlinx.serialization.Serializable

/**
 * A logical snapshot of the user's financial data — the wire format for backups, decoupled from the
 * Room entities so the schema can evolve independently. Records mirror the 5 financial tables
 * (incl. soft-delete tombstones); added fields use defaults so older backups decode forward.
 * Excludes ephemeral/derived tables (notifications, category limit alerts) and app settings.
 */
@Serializable
data class BackupData(
    val categories: List<CategoryRecord> = emptyList(),
    val categoryLimits: List<CategoryLimitRecord> = emptyList(),
    val brands: List<BrandRecord> = emptyList(),
    val transactions: List<TransactionRecord> = emptyList(),
    val smsMessages: List<SmsMessageRecord> = emptyList(),
) {
    val totalRecords: Int
        get() = categories.size + categoryLimits.size + brands.size + transactions.size + smsMessages.size
}

@Serializable
data class CategoryRecord(
    val id: String,
    val name: String,
    val type: String,
    val color: String,
    val icon: String,
    val updatedAtMillis: Long,
    val isDirty: Boolean,
    val deletedAtMillis: Long? = null,
    val serverId: String? = null,
    val version: Long = 0,
)

@Serializable
data class CategoryLimitRecord(
    val categoryId: String,
    val effectiveFrom: Int,
    val amountMinor: Long? = null,
    val currency: String,
)

@Serializable
data class BrandRecord(
    val id: String,
    val name: String,
    val categoryId: String? = null,
    val updatedAtMillis: Long,
    val isDirty: Boolean,
    val deletedAtMillis: Long? = null,
    val serverId: String? = null,
    val version: Long = 0,
)

@Serializable
data class TransactionRecord(
    val id: String,
    val amountMinor: Long,
    val currency: String,
    val brandId: String,
    val note: String? = null,
    val occurredAtMillis: Long,
    val sourceSmsId: String? = null,
    val updatedAtMillis: Long,
    val isDirty: Boolean,
    val deletedAtMillis: Long? = null,
    val serverId: String? = null,
    val version: Long = 0,
)

@Serializable
data class SmsMessageRecord(
    val id: String,
    val body: String,
    val receivedAtMillis: Long,
    val transactionId: String? = null,
    val parsedBrandName: String? = null,
    val parsedAmountMinor: Long? = null,
    val parsedCurrency: String? = null,
    val parsedOccurredAtMillis: Long? = null,
    val updatedAtMillis: Long,
    val isDirty: Boolean,
    val deletedAtMillis: Long? = null,
    val serverId: String? = null,
    val version: Long = 0,
)
