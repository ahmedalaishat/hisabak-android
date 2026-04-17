package com.hisabak.feature.category.presentation.edit

import androidx.lifecycle.viewModelScope
import com.hisabak.core.common.DomainResult
import com.hisabak.core.presentation.BaseViewModel
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.usecase.CreateCategoryUseCase
import com.hisabak.feature.category.domain.usecase.UpdateCategoryUseCase
import kotlinx.coroutines.launch

class CategoryEditViewModel(
    private val categoryId: CategoryId?,
    private val categoryRepository: CategoryRepository,
    private val createCategory: CreateCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase,
) : BaseViewModel<CategoryEditIntent, CategoryEditUiState, CategoryEditEffect>() {

    override fun initialState() = CategoryEditUiState(isNew = categoryId == null)

    init {
        if (categoryId != null) loadExisting(categoryId)
    }

    override fun onIntent(intent: CategoryEditIntent) {
        when (intent) {
            is CategoryEditIntent.NameChanged ->
                setState { copy(nameInput = intent.value, nameError = null) }
            is CategoryEditIntent.TypeChanged ->
                setState { copy(type = intent.value) }
            is CategoryEditIntent.ColorChanged ->
                setState { copy(color = intent.value) }
            is CategoryEditIntent.IconChanged ->
                setState { copy(icon = intent.value) }
            CategoryEditIntent.Save -> save()
            CategoryEditIntent.ConsumeEffect -> clearEffect()
        }
    }

    private fun loadExisting(id: CategoryId) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = categoryRepository.getById(id)) {
                is DomainResult.Success -> {
                    val c = result.value
                    setState {
                        copy(
                            isLoading = false,
                            nameInput = c.name,
                            type = c.type,
                            color = c.color,
                            icon = c.icon,
                        )
                    }
                }
                is DomainResult.Failure -> setState {
                    copy(isLoading = false, generalError = result.error.message)
                }
            }
        }
    }

    private fun save() {
        val s = state.value
        val name = s.nameInput.trim()
        if (name.isEmpty()) {
            setState { copy(nameError = "Name is required") }
            return
        }
        setState { copy(isSaving = true, generalError = null) }
        viewModelScope.launch {
            val result: DomainResult<Unit> = if (categoryId == null) {
                createCategory(name = name, type = s.type, color = s.color, icon = s.icon).map { }
            } else {
                when (val existing = categoryRepository.getById(categoryId)) {
                    is DomainResult.Success -> updateCategory(
                        existing.value.copy(name = name, type = s.type, color = s.color, icon = s.icon),
                    )
                    is DomainResult.Failure -> DomainResult.Failure(existing.error)
                }
            }

            when (result) {
                is DomainResult.Success -> {
                    setState { copy(isSaving = false) }
                    sendEffect(CategoryEditEffect.Saved)
                }
                is DomainResult.Failure -> setState {
                    copy(isSaving = false, generalError = result.error.message)
                }
            }
        }
    }
}
