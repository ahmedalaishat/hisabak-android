package com.hisabak.feature.brand.presentation.list

import com.hisabak.core.common.DomainError
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.brand.domain.usecase.DeleteBrandUseCase
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import com.hisabak.feature.transaction.domain.usecase.ReassignBrandTransactionsUseCase
import com.hisabak.testutil.FakeBrandRepository
import com.hisabak.testutil.FakeCategoryRepository
import com.hisabak.testutil.FakeTransactionRepository
import com.hisabak.testutil.MainDispatcherRule
import com.hisabak.testutil.brand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BrandListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val brandRepo = FakeBrandRepository(listOf(brand(id = "b1", name = "Lulu")))
    private val catRepo = FakeCategoryRepository()
    private val txRepo = FakeTransactionRepository()

    private fun viewModel() = BrandListViewModel(
        observeBrands = ObserveBrandsUseCase(brandRepo),
        observeCategories = ObserveCategoriesUseCase(catRepo),
        observeTransactions = ObserveTransactionsUseCase(txRepo),
        deleteBrand = DeleteBrandUseCase(brandRepo),
        reassignBrandTransactions = ReassignBrandTransactionsUseCase(txRepo),
    )

    @Test
    fun `a successful delete emits no error effect`() = runTest {
        val vm = viewModel()
        advanceUntilIdle()

        vm.onIntent(BrandListIntent.Delete(BrandId("b1")))
        advanceUntilIdle()

        assertTrue(brandRepo.current.isEmpty())
        assertNull(vm.effect.value)
    }

    @Test
    fun `a failed delete surfaces a message instead of failing silently`() = runTest {
        brandRepo.deleteResult = DomainResult.Failure(DomainError.Unexpected(IllegalStateException("FK")))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onIntent(BrandListIntent.Delete(BrandId("b1")))
        advanceUntilIdle()

        assertTrue(brandRepo.current.isNotEmpty()) // brand kept
        assertTrue(vm.effect.value is BrandListEffect.Message)
    }
}
