package com.hisabak.feature.transaction.data

import com.hisabak.core.common.DomainError
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.transaction.domain.PagedTransactions
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionFilter
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryTransactionRepository(
    seed: List<Transaction> = emptyList(),
) : TransactionRepository {

    private val state = MutableStateFlow(seed.associateBy { it.id })

    fun snapshot(): List<Transaction> = state.value.values.toList()

    override fun observe(filter: TransactionFilter): Flow<List<Transaction>> =
        state.asStateFlow().map { it.values.applyFilter(filter) }

    override suspend fun getPage(
        filter: TransactionFilter,
        page: Int,
        perPage: Int,
    ): DomainResult<PagedTransactions> {
        val all = state.value.values.applyFilter(filter)
        val from = ((page - 1) * perPage).coerceAtLeast(0)
        val to = (from + perPage).coerceAtMost(all.size)
        val items = if (from >= all.size) emptyList() else all.subList(from, to)
        return DomainResult.Success(
            PagedTransactions(
                items = items,
                page = page,
                perPage = perPage,
                total = all.size.toLong(),
            ),
        )
    }

    override suspend fun getById(id: TransactionId): DomainResult<Transaction> =
        state.value[id]
            ?.let { DomainResult.Success(it) }
            ?: DomainResult.Failure(DomainError.NotFound("Transaction", id.value))

    override suspend fun upsert(transaction: Transaction): DomainResult<Unit> {
        state.update { it + (transaction.id to transaction) }
        return DomainResult.Success(Unit)
    }

    override suspend fun delete(id: TransactionId): DomainResult<Unit> {
        state.update { it - id }
        return DomainResult.Success(Unit)
    }

    private fun Collection<Transaction>.applyFilter(filter: TransactionFilter): List<Transaction> =
        asSequence()
            .filter { tx -> filter.brandId?.let { tx.brandId == it } ?: true }
            .filter { tx -> filter.dateFrom?.let { tx.occurredAt >= it } ?: true }
            .filter { tx -> filter.dateTo?.let { tx.occurredAt <= it } ?: true }
            .filter { tx ->
                filter.search?.let { q ->
                    tx.note?.contains(q, ignoreCase = true) == true
                } ?: true
            }
            .sortedByDescending { it.occurredAt }
            .toList()
}
