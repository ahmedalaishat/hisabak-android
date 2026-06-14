package com.hisabak.feature.category

import com.hisabak.feature.category.data.RoomCategoryRepository
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.usecase.CreateCategoryUseCase
import com.hisabak.feature.category.domain.usecase.DeleteCategoryUseCase
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.category.domain.usecase.UpdateCategoryUseCase
import com.hisabak.feature.category.presentation.edit.CategoryEditViewModel
import com.hisabak.feature.category.presentation.list.CategoryListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    single<CategoryRepository> { RoomCategoryRepository(dao = get()) }

    factory { ObserveCategoriesUseCase(get()) }
    factory { CreateCategoryUseCase(get(), get()) }
    factory { UpdateCategoryUseCase(get(), get()) }
    factory { DeleteCategoryUseCase(get()) }

    viewModel {
        CategoryListViewModel(
            observeCategories = get(),
            observeBrands = get(),
            observeTransactions = get(),
            deleteCategory = get(),
        )
    }

    viewModel { (categoryId: CategoryId?) ->
        CategoryEditViewModel(
            categoryId = categoryId,
            categoryRepository = get(),
            createCategory = get(),
            updateCategory = get(),
        )
    }
}
