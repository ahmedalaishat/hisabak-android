package com.hisabak.feature.category.data

import com.hisabak.core.common.DomainError
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryCategoryRepository(
    seed: List<Category> = emptyList(),
) : CategoryRepository {

    private val state = MutableStateFlow(seed.associateBy { it.id })

    override fun observeAll(type: CategoryType?, search: String?): Flow<List<Category>> =
        state.asStateFlow().map { categories ->
            categories.values.asSequence()
                .filter { c -> type?.let { c.type == it } ?: true }
                .filter { c -> search?.let { q -> c.name.contains(q, ignoreCase = true) } ?: true }
                .sortedBy { it.name.lowercase() }
                .toList()
        }

    override suspend fun getById(id: CategoryId): DomainResult<Category> =
        state.value[id]
            ?.let { DomainResult.Success(it) }
            ?: DomainResult.Failure(DomainError.NotFound("Category", id.value))

    override suspend fun upsert(category: Category): DomainResult<Unit> {
        state.update { it + (category.id to category) }
        return DomainResult.Success(Unit)
    }

    override suspend fun delete(id: CategoryId): DomainResult<Unit> {
        state.update { it - id }
        return DomainResult.Success(Unit)
    }
}
