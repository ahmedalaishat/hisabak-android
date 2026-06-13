package com.hisabak.feature.brand.presentation.list

import androidx.lifecycle.viewModelScope
import com.hisabak.core.presentation.BaseViewModel
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.usecase.DeleteBrandUseCase
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BrandListViewModel(
    private val observeBrands: ObserveBrandsUseCase,
    private val observeCategories: ObserveCategoriesUseCase,
    private val deleteBrand: DeleteBrandUseCase,
) : BaseViewModel<BrandListIntent, BrandListUiState, BrandListEffect>() {

    override fun initialState() = BrandListUiState()

    init {
        observeRows()
        observeAvailableCategoryOptions()
    }

    override fun onIntent(intent: BrandListIntent) {
        when (intent) {
            is BrandListIntent.SearchChanged ->
                setState { copy(search = intent.query) }
            is BrandListIntent.CategoryFilterChanged ->
                setState { copy(categoryFilter = intent.categoryId) }
            is BrandListIntent.Delete ->
                viewModelScope.launch { deleteBrand(intent.id) }
            BrandListIntent.ConsumeEffect -> clearEffect()
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    private fun observeRows() {
        val searchFlow = state.map { it.search }.distinctUntilChanged()
            .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
        val filterFlow = state.map { it.categoryFilter }.distinctUntilChanged()

        combine(searchFlow, filterFlow) { search, categoryId -> search to categoryId }
            .flatMapLatest { (search, categoryId) ->
                combine(
                    observeBrands(
                        search = search.takeIf { it.isNotBlank() },
                        categoryId = categoryId,
                    ),
                    observeCategories(),
                ) { brands, categories -> buildRows(brands, categories) }
            }
            .onEach { rows -> setState { copy(rows = rows, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    private fun observeAvailableCategoryOptions() {
        observeCategories()
            .onEach { categories ->
                val options = categories.map {
                    BrandListUiState.CategoryOption(id = it.id, name = it.name, color = it.color)
                }
                setState { copy(availableCategories = options) }
            }
            .launchIn(viewModelScope)
    }

    private fun buildRows(brands: List<Brand>, categories: List<Category>): List<BrandRow> {
        val byId: Map<CategoryId, Category> = categories.associateBy { it.id }
        return brands.map { brand ->
            val category = brand.categoryId?.let { byId[it] }
            BrandRow(
                id = brand.id,
                name = brand.name,
                categoryId = brand.categoryId,
                categoryName = category?.name,
                categoryColor = category?.color,
                categoryIcon = category?.icon,
            )
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 250L
    }
}
