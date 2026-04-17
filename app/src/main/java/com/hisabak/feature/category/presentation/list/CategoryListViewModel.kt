package com.hisabak.feature.category.presentation.list

import androidx.lifecycle.viewModelScope
import com.hisabak.core.presentation.BaseViewModel
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.usecase.DeleteCategoryUseCase
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val observeCategories: ObserveCategoriesUseCase,
    private val deleteCategory: DeleteCategoryUseCase,
) : BaseViewModel<CategoryListIntent, CategoryListUiState, CategoryListEffect>() {

    override fun initialState() = CategoryListUiState()

    init {
        observeRowsBasedOnSearchAndFilter()
    }

    override fun onIntent(intent: CategoryListIntent) {
        when (intent) {
            is CategoryListIntent.SearchChanged ->
                setState { copy(search = intent.query) }
            is CategoryListIntent.TypeFilterChanged ->
                setState { copy(typeFilter = intent.type) }
            is CategoryListIntent.Delete ->
                viewModelScope.launch { deleteCategory(intent.id) }
            CategoryListIntent.ConsumeEffect -> clearEffect()
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    private fun observeRowsBasedOnSearchAndFilter() {
        val searchFlow = state.map { it.search }.distinctUntilChanged()
            .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
        val filterFlow = state.map { it.typeFilter }.distinctUntilChanged()

        combine(searchFlow, filterFlow) { search, type -> search to type }
            .flatMapLatest { (search, type) ->
                observeCategories(
                    type = type,
                    search = search.takeIf { it.isNotBlank() },
                )
            }
            .onEach { categories -> setState { copy(rows = buildRows(categories), isLoading = false) } }
            .launchIn(viewModelScope)
    }

    private fun buildRows(categories: List<Category>): List<CategoryRow> =
        categories.map {
            CategoryRow(
                id = it.id,
                name = it.name,
                type = it.type,
                color = it.color,
                icon = it.icon,
            )
        }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 250L
    }
}
