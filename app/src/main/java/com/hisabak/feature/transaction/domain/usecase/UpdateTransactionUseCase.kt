package com.hisabak.feature.transaction.domain.usecase

import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionRepository

class UpdateTransactionUseCase(
    private val repository: TransactionRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(transaction: Transaction): DomainResult<Unit> {
        val updated = transaction.copy(
            sync = transaction.sync.copy(updatedAt = clock.now(), isDirty = true),
        )
        return repository.upsert(updated)
    }
}
