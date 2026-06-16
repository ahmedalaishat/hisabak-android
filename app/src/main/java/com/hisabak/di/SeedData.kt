package com.hisabak.di

import com.hisabak.core.common.Clock
import com.hisabak.core.common.Currency
import com.hisabak.core.common.Money
import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.TransactionId
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import kotlin.random.Random

/**
 * Demo seed data — in-memory only. Replace when the DB layer lands.
 *
 * Generates a realistic transaction history from January 2024 through today so the
 * dashboard period filters (this month / last month / this year / last year / all time)
 * all have data to show.
 */
class SeedData(clock: Clock, private val currency: Currency) {
    private val now = clock.now()
    private val zone = ZoneId.systemDefault()
    private val today = LocalDate.ofInstant(now, zone)
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
    private val savings = categories[4]

    val brands: List<Brand> = listOf(
        Brand(BrandId.new(), "Whole Foods", groceries.id, sync),
        Brand(BrandId.new(), "Trader Joe's", groceries.id, sync),
        Brand(BrandId.new(), "Acme Corp", salary.id, sync),
        Brand(BrandId.new(), "Uber", transport.id, sync),
        Brand(BrandId.new(), "Starbucks", dining.id, sync),
        Brand(BrandId.new(), "Chipotle", dining.id, sync),
        Brand(BrandId.new(), "Vault", savings.id, sync),
    )

    private val wholeFoods = brands[0]
    private val traderJoes = brands[1]
    private val acme = brands[2]
    private val uber = brands[3]
    private val starbucks = brands[4]
    private val chipotle = brands[5]
    private val vault = brands[6]

    val transactions: List<Transaction> = generate()

    private fun generate(): List<Transaction> {
        val rnd = Random(seed = 42)
        val out = mutableListOf<Transaction>()
        var month = YearMonth.of(2024, 1)
        val endMonth = YearMonth.from(today)

        fun add(brandId: BrandId, amountMinor: Long, date: LocalDate, note: String?) {
            if (date.isAfter(today)) return
            out += tx(brandId, amountMinor, date, note)
        }

        while (!month.isAfter(endMonth)) {
            val len = month.lengthOfMonth()

            // Salary on the 25th.
            add(acme.id, 500000L + rnd.nextInt(0, 25_000), month.atDay(minOf(25, len)), "Monthly salary")

            // Monthly transfer to savings on the 26th.
            add(vault.id, 100000L + rnd.nextInt(0, 50_000), month.atDay(minOf(26, len)), "Monthly savings")

            // Weekly groceries.
            for (week in 0..3) {
                val store = if (rnd.nextBoolean()) wholeFoods else traderJoes
                add(store.id, (3000 + rnd.nextInt(0, 3500)).toLong(), month.atDay(minOf(3 + week * 7, len)), "Weekly groceries")
            }

            // Coffee a few mornings a week.
            for (day in listOf(2, 9, 16, 23)) {
                add(starbucks.id, (450 + rnd.nextInt(0, 400)).toLong(), month.atDay(minOf(day, len)), "Morning coffee")
            }

            // Lunches out.
            for (day in listOf(6, 14, 21, 28)) {
                add(chipotle.id, (1100 + rnd.nextInt(0, 900)).toLong(), month.atDay(minOf(day, len)), "Lunch")
            }

            // Rides through the month.
            for (day in listOf(5, 12, 19, 27)) {
                add(uber.id, (1200 + rnd.nextInt(0, 2500)).toLong(), month.atDay(minOf(day, len)), null)
            }

            month = month.plusMonths(1)
        }
        return out
    }

    private fun tx(brandId: BrandId, amountMinor: Long, date: LocalDate, note: String?): Transaction =
        Transaction(
            id = TransactionId.new(),
            amount = Money(amountMinor, currency),
            brandId = brandId,
            note = note,
            occurredAt = date.atTime(12, 0).atZone(zone).toInstant(),
            sync = sync,
        )
}
