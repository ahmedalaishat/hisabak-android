package com.hisabak.feature.transaction.data.local

import com.hisabak.core.common.Currency
import com.hisabak.core.common.Money
import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionId
import java.time.Instant

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = TransactionId(id),
    amount = Money(amountMinor, Currency(currency)),
    brandId = BrandId(brandId),
    note = note,
    occurredAt = Instant.ofEpochMilli(occurredAtMillis),
    sourceSmsId = sourceSmsId,
    sync = SyncMetadata(
        updatedAt = Instant.ofEpochMilli(updatedAtMillis),
        isDirty = isDirty,
        deletedAt = deletedAtMillis?.let(Instant::ofEpochMilli),
        serverId = serverId,
        version = version,
    ),
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id.value,
    amountMinor = amount.amountMinor,
    currency = amount.currency.code,
    brandId = brandId.value,
    note = note,
    occurredAtMillis = occurredAt.toEpochMilli(),
    sourceSmsId = sourceSmsId,
    updatedAtMillis = sync.updatedAt.toEpochMilli(),
    isDirty = sync.isDirty,
    deletedAtMillis = sync.deletedAt?.toEpochMilli(),
    serverId = sync.serverId,
    version = sync.version,
)
