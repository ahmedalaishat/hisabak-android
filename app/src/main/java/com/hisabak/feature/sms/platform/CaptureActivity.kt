package com.hisabak.feature.sms.platform

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.hisabak.core.common.DomainResult
import com.hisabak.feature.sms.domain.capture.CaptureSource
import com.hisabak.feature.sms.domain.capture.CaptureTransactionUseCase
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Permission-free capture entry point for text **shared** into the app (`ACTION_SEND`) or
 * **selected** in another app (`ACTION_PROCESS_TEXT`). A thin platform adapter over
 * [CaptureTransactionUseCase]: extract the text, ingest it, toast the outcome, finish.
 *
 * Present in every build (needs no permission) and the only compliant near-automatic capture in
 * the Play build, where the SMS broadcast receiver is absent.
 */
class CaptureActivity : ComponentActivity() {

    private val capture: CaptureTransactionUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val (text, source) = extract(intent)
        if (text.isNullOrBlank()) {
            finish()
            return
        }
        lifecycleScope.launch {
            val message = when (capture(text.toString(), source)) {
                is DomainResult.Success -> "Transaction saved"
                is DomainResult.Failure -> "Couldn't read a transaction from that text"
            }
            Toast.makeText(this@CaptureActivity, message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun extract(intent: Intent): Pair<CharSequence?, CaptureSource> = when (intent.action) {
        Intent.ACTION_PROCESS_TEXT ->
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) to CaptureSource.PROCESS_TEXT
        else ->
            intent.getCharSequenceExtra(Intent.EXTRA_TEXT) to CaptureSource.SHARE
    }
}
