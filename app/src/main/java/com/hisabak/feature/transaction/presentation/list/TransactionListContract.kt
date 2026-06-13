package com.hisabak.feature.transaction.presentation.list

import com.hisabak.core.common.Money
import com.hisabak.core.presentation.ViewEffect
import com.hisabak.core.presentation.ViewIntent
import com.hisabak.core.presentation.ViewState
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
    val categoryIcon: String?,
    val note: String?,
    val occurredAt: Instant,
)

data class TransactionListUiState(
    val rows: List<TransactionRow> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
) : ViewState

sealed interface TransactionListIntent : ViewIntent {
    data class SearchChanged(val query: String) : TransactionListIntent
    data class Delete(val id: TransactionId) : TransactionListIntent
    data object ConsumeEffect : TransactionListIntent
}

sealed interface TransactionListEffect : ViewEffect {
    // No one-shot effects yet. Add here as nav / snackbar needs appear.
}
