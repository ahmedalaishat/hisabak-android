package com.hisabak.feature.brand.domain

import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.core.common.SyncMetadata

data class Brand(
    val id: BrandId,
    val name: String,
    val categoryId: CategoryId?,
    val sync: SyncMetadata,
) {
    init {
        require(name.isNotBlank()) { "Brand name must not be blank" }
    }
}
