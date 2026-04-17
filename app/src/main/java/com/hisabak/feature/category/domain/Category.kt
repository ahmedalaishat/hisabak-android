package com.hisabak.feature.category.domain

import com.hisabak.core.common.SyncMetadata

data class Category(
    val id: CategoryId,
    val name: String,
    val type: CategoryType,
    val color: String = DEFAULT_COLOR,
    val icon: String = DEFAULT_ICON,
    val sync: SyncMetadata,
) {
    init {
        require(name.isNotBlank()) { "Category name must not be blank" }
    }

    companion object {
        const val DEFAULT_COLOR = "gray"
        const val DEFAULT_ICON = "wallet"
    }
}
