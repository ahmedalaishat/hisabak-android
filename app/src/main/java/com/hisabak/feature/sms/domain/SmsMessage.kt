package com.hisabak.feature.sms.domain

import com.hisabak.core.common.SyncMetadata
import com.hisabak.feature.transaction.domain.TransactionId
import java.time.Instant

data class SmsMessage(
    val id: SmsMessageId,
    val body: String,
    val receivedAt: Instant,
    val transactionId: TransactionId? = null,
    val parsed: ParsedSmsData? = null,
    val sync: SyncMetadata,
) {
    val isLinked: Boolean get() = transactionId != null
}
