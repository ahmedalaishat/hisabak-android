package com.hisabak.feature.transaction.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionFilter
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionListViewModel(
    observeTransactions: ObserveTransactionsUseCase,
    observeBrands: ObserveBrandsUseCase,
    observeCategories: ObserveCategoriesUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    val uiState: StateFlow<TransactionListUiState> = _searchQuery
        .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val filter = if (query.isBlank()) TransactionFilter.NONE
            else TransactionFilter(search = query)
            combine(
                observeTransactions(filter),
                observeBrands(),
                observeCategories(),
            ) { txs, brands, categories ->
                TransactionListUiState(
                    rows = buildRows(txs, brands, categories),
                    isLoading = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionListUiState(isLoading = true),
        )

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    fun onDelete(id: TransactionId) {
        viewModelScope.launch { deleteTransaction(id) }
    }

    private fun buildRows(
        txs: List<Transaction>,
        brands: List<Brand>,
        categories: List<Category>,
    ): List<TransactionRow> {
        val brandsById: Map<BrandId, Brand> = brands.associateBy { it.id }
        val categoriesById: Map<CategoryId, Category> = categories.associateBy { it.id }
        return txs.map { tx ->
            val brand = brandsById[tx.brandId]
            val category = brand?.categoryId?.let { categoriesById[it] }
            TransactionRow(
                id = tx.id,
                amount = tx.amount,
                brandName = brand?.name ?: "Unknown",
                categoryName = category?.name,
                categoryType = category?.type,
                categoryColor = category?.color,
                note = tx.note,
                occurredAt = tx.occurredAt,
            )
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 250L
    }
}
