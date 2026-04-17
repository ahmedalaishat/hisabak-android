package com.hisabak.feature.transaction.presentation.list

import com.hisabak.core.common.Money
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.domain.TransactionId
import java.time.Instant

data class TransactionRow(
    val id: TransactionId,
    val amount: Money,
    val brandName: String,
    val categoryName: String?,
    val categoryType: CategoryType?,
    val categoryColor: String?,
    val note: String?,
    val occurredAt: Instant,
)

data class TransactionListUiState(
    val rows: List<TransactionRow> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
)
