package com.hisabak.feature.category.domain.usecase

import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult

class UpdateCategoryUseCase(
    private val repository: CategoryRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(category: Category): DomainResult<Unit> {
        val updated = category.copy(
            sync = category.sync.copy(updatedAt = clock.now(), isDirty = true),
        )
        return repository.upsert(updated)
    }
}
