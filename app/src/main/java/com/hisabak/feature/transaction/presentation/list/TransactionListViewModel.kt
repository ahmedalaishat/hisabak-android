package com.hisabak.feature.transaction.presentation.list

import androidx.lifecycle.viewModelScope
import com.hisabak.core.presentation.BaseViewModel
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionFilter
import com.hisabak.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val observeTransactions: ObserveTransactionsUseCase,
    private val observeBrands: ObserveBrandsUseCase,
    private val observeCategories: ObserveCategoriesUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
) : BaseViewModel<TransactionListIntent, TransactionListUiState, TransactionListEffect>() {

    override fun initialState() = TransactionListUiState()

    init {
        observeRowsBasedOnSearch()
    }

    override fun onIntent(intent: TransactionListIntent) {
        when (intent) {
            is TransactionListIntent.SearchChanged ->
                setState { copy(search = intent.query) }
            is TransactionListIntent.Delete ->
                viewModelScope.launch { deleteTransaction(intent.id) }
            TransactionListIntent.ConsumeEffect -> clearEffect()
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    private fun observeRowsBasedOnSearch() {
        state
            .map { it.search }
            .distinctUntilChanged()
            .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
            .flatMapLatest { query ->
                val filter = if (query.isBlank()) TransactionFilter.NONE
                else TransactionFilter(search = query)
                combine(
                    observeTransactions(filter),
                    observeBrands(),
                    observeCategories(),
                ) { txs, brands, categories -> buildRows(txs, brands, categories) }
            }
            .onEach { rows -> setState { copy(rows = rows, isLoading = false) } }
            .launchIn(viewModelScope)
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
