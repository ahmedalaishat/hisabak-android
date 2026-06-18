package com.hisabak.core.data.local

import com.hisabak.core.common.Clock
import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType

/**
 * Minimal starter categories for a fresh **production** install — one usable default for every
 * [CategoryType] so a new user can record a transaction immediately instead of facing an empty
 * app. Unlike [com.hisabak.di.SeedData] this carries no brands, transactions, or limits: it's a
 * neutral starting point the user freely edits or deletes, not demo data.
 */
class StarterData(clock: Clock) {
    private val sync = SyncMetadata(updatedAt = clock.now())

    val categories: List<Category> = listOf(
        Category(CategoryId.new(), "Salary", CategoryType.INCOME, color = "blue", icon = "briefcase", sync = sync),
        Category(CategoryId.new(), "Groceries", CategoryType.EXPENSES, color = "green", icon = "cart", sync = sync),
        Category(CategoryId.new(), "Dining", CategoryType.EXPENSES, color = "red", icon = "utensils", sync = sync),
        Category(CategoryId.new(), "Transport", CategoryType.EXPENSES, color = "orange", icon = "car", sync = sync),
        Category(CategoryId.new(), "Shopping", CategoryType.EXPENSES, color = "pink", icon = "gift", sync = sync),
        Category(CategoryId.new(), "Savings", CategoryType.SAVINGS, color = "teal", icon = "piggy-bank", sync = sync),
        Category(CategoryId.new(), "Investments", CategoryType.INVESTMENT, color = "purple", icon = "wallet", sync = sync),
    )
}
