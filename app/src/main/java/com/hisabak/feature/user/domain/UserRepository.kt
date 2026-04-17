package com.hisabak.feature.user.domain

import com.hisabak.core.common.DomainResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observe(): Flow<UserProfile?>
    suspend fun get(): DomainResult<UserProfile>
    suspend fun update(profile: UserProfile): DomainResult<Unit>
}
