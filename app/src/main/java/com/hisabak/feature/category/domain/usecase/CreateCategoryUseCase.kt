package com.hisabak.feature.category.domain.usecase

import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.core.common.SyncMetadata

class CreateCategoryUseCase(
    private val repository: CategoryRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(
        name: String,
        type: CategoryType,
        color: String = Category.DEFAULT_COLOR,
        icon: String = Category.DEFAULT_ICON,
    ): DomainResult<Category> {
        val category = Category(
            id = CategoryId.new(),
            name = name,
            type = type,
            color = color,
            icon = icon,
            sync = SyncMetadata(updatedAt = clock.now()),
        )
        return repository.upsert(category).map { category }
    }
}
