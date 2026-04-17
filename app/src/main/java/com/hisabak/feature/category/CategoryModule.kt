package com.hisabak.feature.category

import com.hisabak.di.SeedData
import com.hisabak.feature.category.data.InMemoryCategoryRepository
import com.hisabak.feature.category.domain.CategoryRepository
import com.hisabak.feature.category.domain.usecase.CreateCategoryUseCase
import com.hisabak.feature.category.domain.usecase.DeleteCategoryUseCase
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.category.domain.usecase.UpdateCategoryUseCase
import org.koin.dsl.module

val categoryModule = module {
    single<CategoryRepository> { InMemoryCategoryRepository(seed = get<SeedData>().categories) }

    factory { ObserveCategoriesUseCase(get()) }
    factory { CreateCategoryUseCase(get(), get()) }
    factory { UpdateCategoryUseCase(get(), get()) }
    factory { DeleteCategoryUseCase(get()) }
}
