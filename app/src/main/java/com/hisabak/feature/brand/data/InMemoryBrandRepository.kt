package com.hisabak.feature.brand.data

import com.hisabak.core.common.DomainError
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.feature.category.domain.CategoryId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryBrandRepository(
    seed: List<Brand> = emptyList(),
    private val transactionCount: suspend (BrandId) -> Long = { 0L },
) : BrandRepository {

    private val state = MutableStateFlow(seed.associateBy { it.id })

    override fun observeAll(search: String?, categoryId: CategoryId?): Flow<List<Brand>> =
        state.asStateFlow().map { brands ->
            brands.values.asSequence()
                .filter { b -> categoryId?.let { b.categoryId == it } ?: true }
                .filter { b -> search?.let { q -> b.name.contains(q, ignoreCase = true) } ?: true }
                .sortedBy { it.name.lowercase() }
                .toList()
        }

    override suspend fun getById(id: BrandId): DomainResult<Brand> =
        state.value[id]
            ?.let { DomainResult.Success(it) }
            ?: DomainResult.Failure(DomainError.NotFound("Brand", id.value))

    override suspend fun findByNameLike(name: String): Brand? {
        val q = name.trim()
        if (q.isEmpty()) return null
        return state.value.values.firstOrNull {
            it.name.equals(q, ignoreCase = true) ||
                it.name.contains(q, ignoreCase = true) ||
                q.contains(it.name, ignoreCase = true)
        }
    }

    override suspend fun upsert(brand: Brand): DomainResult<Unit> {
        state.update { it + (brand.id to brand) }
        return DomainResult.Success(Unit)
    }

    override suspend fun delete(id: BrandId): DomainResult<Unit> {
        state.update { it - id }
        return DomainResult.Success(Unit)
    }

    override suspend fun countTransactions(id: BrandId): Long = transactionCount(id)
}
