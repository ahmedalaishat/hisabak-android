package com.hisabak.feature.sms.domain

import com.hisabak.core.common.DomainResult
import kotlinx.coroutines.flow.Flow

interface SmsRepository {
    fun observeAll(search: String? = null): Flow<List<SmsMessage>>
    suspend fun getById(id: SmsMessageId): DomainResult<SmsMessage>
    suspend fun upsert(message: SmsMessage): DomainResult<Unit>
    suspend fun delete(id: SmsMessageId): DomainResult<Unit>
}
