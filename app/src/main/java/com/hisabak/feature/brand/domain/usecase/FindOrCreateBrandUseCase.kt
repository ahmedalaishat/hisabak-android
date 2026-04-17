package com.hisabak.feature.brand.domain.usecase

import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.core.common.SyncMetadata

/**
 * Mirrors Hisabi's Brand::findOrCreateNew — tries fuzzy name match first, creates if missing.
 */
class FindOrCreateBrandUseCase(
    private val repository: BrandRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(name: String): DomainResult<Brand> {
        val normalized = name.trim()
        if (normalized.isEmpty()) return DomainResult.Failure(
            com.hisabak.core.common.DomainError.ValidationFailed("Brand name required")
        )
        repository.findByNameLike(normalized)?.let { return DomainResult.Success(it) }
        val brand = Brand(
            id = BrandId.new(),
            name = normalized,
            categoryId = null,
            sync = SyncMetadata(updatedAt = clock.now()),
        )
        return repository.upsert(brand).map { brand }
    }
}
