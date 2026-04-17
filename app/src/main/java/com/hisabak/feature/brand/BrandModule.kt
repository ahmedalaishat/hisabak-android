package com.hisabak.feature.brand

import com.hisabak.di.SeedData
import com.hisabak.feature.brand.data.InMemoryBrandRepository
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.feature.brand.domain.usecase.CreateBrandUseCase
import com.hisabak.feature.brand.domain.usecase.DeleteBrandUseCase
import com.hisabak.feature.brand.domain.usecase.FindOrCreateBrandUseCase
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.brand.domain.usecase.UpdateBrandUseCase
import org.koin.dsl.module

val brandModule = module {
    single<BrandRepository> { InMemoryBrandRepository(seed = get<SeedData>().brands) }

    factory { ObserveBrandsUseCase(get()) }
    factory { CreateBrandUseCase(get(), get()) }
    factory { UpdateBrandUseCase(get(), get()) }
    factory { DeleteBrandUseCase(get()) }
    factory { FindOrCreateBrandUseCase(get(), get()) }
}
