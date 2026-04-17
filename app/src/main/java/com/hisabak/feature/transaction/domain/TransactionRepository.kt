package com.hisabak.feature.transaction.domain

import com.hisabak.core.common.DomainResult
import kotlinx.coroutines.flow.Flow

data class PagedTransactions(
    val items: List<Transaction>,
    val page: Int,
    val perPage: Int,
    val total: Long,
)

interface TransactionRepository {
    fun observe(filter: TransactionFilter = TransactionFilter.NONE): Flow<List<Transaction>>
    suspend fun getPage(filter: TransactionFilter, page: Int, perPage: Int): DomainResult<PagedTransactions>
    suspend fun getById(id: TransactionId): DomainResult<Transaction>
    suspend fun upsert(transaction: Transaction): DomainResult<Unit>
    suspend fun delete(id: TransactionId): DomainResult<Unit>
}
