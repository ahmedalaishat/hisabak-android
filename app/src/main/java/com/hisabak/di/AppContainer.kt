package com.hisabak.di

import com.hisabak.core.common.Clock
import com.hisabak.core.common.Currency
import com.hisabak.core.common.Money
import com.hisabak.core.common.SystemClock
import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.brand.data.InMemoryBrandRepository
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.feature.brand.domain.usecase.FindOrCreateBrandUseCase
import com.hisabak.feature.category.data.InMemoryCategoryRepository
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.data.InMemoryTransactionRepository
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.domain.TransactionRepository
import com.hisabak.feature.transaction.domain.usecase.CreateTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import com.hisabak.feature.transaction.domain.usecase.UpdateTransactionUseCase
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Manual service locator. Easy to replace with Koin when we go multi-module/CMP.
 */
class AppContainer {

    val clock: Clock = SystemClock()
    val defaultCurrency: Currency = Currency.USD

    private val seed = SeedData(clock, defaultCurrency)

    val categoryRepository: CategoryRepository = InMemoryCategoryRepository(seed.categories)
    val brandRepository: BrandRepository = InMemoryBrandRepository(seed.brands)
    val transactionRepository: TransactionRepository = InMemoryTransactionRepository(seed.transactions)

    val observeTransactions = ObserveTransactionsUseCase(transactionRepository)
    val createTransaction = CreateTransactionUseCase(transactionRepository, clock)
    val updateTransaction = UpdateTransactionUseCase(transactionRepository, clock)
    val deleteTransaction = DeleteTransactionUseCase(transactionRepository)

    val observeBrands = ObserveBrandsUseCase(brandRepository)
    val findOrCreateBrand = FindOrCreateBrandUseCase(brandRepository, clock)

    val observeCategories = ObserveCategoriesUseCase(categoryRepository)
}

private class SeedData(clock: Clock, currency: Currency) {
    private val now: Instant = clock.now()
    private val sync = SyncMetadata(updatedAt = now)

    val categories: List<Category> = listOf(
        Category(CategoryId.new(), "Groceries", CategoryType.EXPENSES, color = "green", icon = "cart", sync = sync),
        Category(CategoryId.new(), "Salary", CategoryType.INCOME, color = "blue", icon = "briefcase", sync = sync),
        Category(CategoryId.new(), "Transport", CategoryType.EXPENSES, color = "orange", icon = "car", sync = sync),
        Category(CategoryId.new(), "Dining", CategoryType.EXPENSES, color = "red", icon = "utensils", sync = sync),
        Category(CategoryId.new(), "Savings", CategoryType.SAVINGS, color = "teal", icon = "piggy-bank", sync = sync),
    )

    private val groceries = categories[0]
    private val salary = categories[1]
    private val transport = categories[2]
    private val dining = categories[3]

    val brands: List<Brand> = listOf(
        Brand(BrandId.new(), "Whole Foods", groceries.id, sync),
        Brand(BrandId.new(), "Trader Joe's", groceries.id, sync),
        Brand(BrandId.new(), "Acme Corp", salary.id, sync),
        Brand(BrandId.new(), "Uber", transport.id, sync),
        Brand(BrandId.new(), "Starbucks", dining.id, sync),
        Brand(BrandId.new(), "Chipotle", dining.id, sync),
    )

    val transactions: List<Transaction> = listOf(
        tx(brands[0].id, 4250, daysAgo = 0, note = "Weekly groceries"),
        tx(brands[4].id, 675, daysAgo = 1, note = "Morning coffee"),
        tx(brands[3].id, 1820, daysAgo = 1, note = null),
        tx(brands[5].id, 1499, daysAgo = 2, note = "Lunch"),
        tx(brands[2].id, 500000, daysAgo = 3, note = "Monthly salary"),
        tx(brands[1].id, 3310, daysAgo = 4, note = null),
        tx(brands[3].id, 2250, daysAgo = 5, note = "Airport ride"),
    )

    private fun tx(brandId: BrandId, amountMinor: Long, daysAgo: Long, note: String?): Transaction =
        Transaction(
            id = TransactionId.new(),
            amount = Money(amountMinor, Currency.USD),
            brandId = brandId,
            note = note,
            occurredAt = now.minus(daysAgo, ChronoUnit.DAYS),
            sync = sync,
        )
}
