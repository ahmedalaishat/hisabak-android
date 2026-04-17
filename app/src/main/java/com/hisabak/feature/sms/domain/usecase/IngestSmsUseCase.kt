package com.hisabak.feature.sms.domain.usecase

import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult
import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.sms.domain.SmsMessage
import com.hisabak.feature.sms.domain.SmsMessageId
import com.hisabak.feature.sms.domain.SmsRepository
import com.hisabak.feature.sms.domain.SmsTransactionProcessor
import com.hisabak.feature.transaction.domain.Transaction
import java.time.Instant

class IngestSmsUseCase(
    private val smsRepository: SmsRepository,
    private val processor: SmsTransactionProcessor,
    private val clock: Clock,
) {
    suspend operator fun invoke(
        body: String,
        receivedAt: Instant? = null,
    ): DomainResult<Transaction> {
        val now = clock.now()
        val message = SmsMessage(
            id = SmsMessageId.new(),
            body = body,
            receivedAt = receivedAt ?: now,
            sync = SyncMetadata(updatedAt = now),
        )
        return smsRepository.upsert(message).flatMap { processor.process(message) }
    }
}
