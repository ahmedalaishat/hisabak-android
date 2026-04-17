package com.hisabak.feature.user.domain.usecase

import com.hisabak.core.common.DomainResult
import com.hisabak.feature.user.domain.UserProfile
import com.hisabak.feature.user.domain.UserRepository

class GetUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): DomainResult<UserProfile> = repository.get()
}
