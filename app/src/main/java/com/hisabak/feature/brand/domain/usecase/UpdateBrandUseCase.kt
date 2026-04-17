package com.hisabak.feature.brand.domain.usecase

import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult

class UpdateBrandUseCase(
    private val repository: BrandRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(brand: Brand): DomainResult<Unit> {
        val updated = brand.copy(
            sync = brand.sync.copy(updatedAt = clock.now(), isDirty = true),
        )
        return repository.upsert(updated)
    }
}
