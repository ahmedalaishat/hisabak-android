package com.hisabak.feature.user.domain.usecase

import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.user.domain.UserProfile
import com.hisabak.feature.user.domain.UserRepository

class UpdateUserProfileUseCase(
    private val repository: UserRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(profile: UserProfile): DomainResult<Unit> {
        val updated = profile.copy(
            sync = profile.sync.copy(updatedAt = clock.now(), isDirty = true),
        )
        return repository.update(updated)
    }
}
