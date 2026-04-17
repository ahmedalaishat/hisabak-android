package com.hisabak.feature.brand.domain.usecase

import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.core.common.SyncMetadata

class CreateBrandUseCase(
    private val repository: BrandRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(name: String, categoryId: CategoryId? = null): DomainResult<Brand> {
        val brand = Brand(
            id = BrandId.new(),
            name = name,
            categoryId = categoryId,
            sync = SyncMetadata(updatedAt = clock.now()),
        )
        return repository.upsert(brand).map { brand }
    }
}
