package com.hisabak.feature.transaction.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hisabak.di.AppContainer
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.transaction.domain.TransactionFilter
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionListViewModel(
    observeTransactions: ObserveTransactionsUseCase,
    observeBrands: ObserveBrandsUseCase,
    observeCategories: ObserveCategoriesUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TransactionListUiState> = searchQuery
        .flatMapLatest { query ->
            val filter = if (query.isBlank()) TransactionFilter.NONE
            else TransactionFilter(search = query)
            combine(
                observeTransactions(filter),
                observeBrands(),
                observeCategories(),
            ) { txs, brands, categories ->
                val brandsById: Map<BrandId, Brand> = brands.associateBy { it.id }
                val categoriesById: Map<CategoryId, Category> = categories.associateBy { it.id }
                val rows = txs.map { tx ->
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
                TransactionListUiState(
                    rows = rows,
                    search = query,
                    isLoading = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionListUiState(search = searchQuery.value),
        )

    fun onSearchChange(query: String) {
        searchQuery.value = query
    }

    fun onDelete(id: TransactionId) {
        viewModelScope.launch { deleteTransaction(id) }
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer {
                TransactionListViewModel(
                    observeTransactions = container.observeTransactions,
                    observeBrands = container.observeBrands,
                    observeCategories = container.observeCategories,
                    deleteTransaction = container.deleteTransaction,
                )
            }
        }
    }
}
