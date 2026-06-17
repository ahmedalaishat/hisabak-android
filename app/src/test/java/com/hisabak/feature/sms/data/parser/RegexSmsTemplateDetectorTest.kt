package com.hisabak.feature.sms.data.parser

import com.hisabak.feature.sms.domain.DefaultSmsTemplates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RegexSmsTemplateDetectorTest {

    // Note: the detector masks `{x}` as non-greedy `(.*?)`, so a placeholder must be followed by
    // literal text to capture anything. A trailing `.` in a template is a regex wildcard, not a
    // literal dot — which is why the last field (e.g. `{time}`) is dropped in some real templates.

    @Test
    fun `extracts placeholder fields bounded by literals`() {
        val detector = RegexSmsTemplateDetector(listOf("Payment of AED {amount} to {brand} on card"))

        val template = detector.detect("Payment of AED 150.00 to Carrefour on card")

        assertEquals("150.00", template?.fields?.get("amount"))
        assertEquals("Carrefour", template?.fields?.get("brand"))
    }

    @Test
    fun `returns null when no template matches`() {
        val detector = RegexSmsTemplateDetector(listOf("Payment of AED {amount} to {brand} now"))

        assertNull(detector.detect("Totally unrelated message"))
    }

    @Test
    fun `first matching template wins`() {
        val detector = RegexSmsTemplateDetector(
            listOf(
                "AED {amount} at {brand} done",
                "AED {amount} at {other} done",
            ),
        )

        val template = detector.detect("AED 10 at Shop done")

        assertEquals("AED {amount} at {brand} done", template?.pattern)
        assertEquals("Shop", template?.fields?.get("brand"))
    }

    @Test
    fun `ignore placeholders are dropped from fields`() {
        val detector = RegexSmsTemplateDetector(
            listOf("Your Cr.Card {card} was used for AED{amount} on {date} at {brand},{ignore} ref"),
        )

        val template = detector.detect("Your Cr.Card 1234 was used for AED50 on 17-06-2026 at Spinneys,99 ref")

        assertEquals("50", template?.fields?.get("amount"))
        assertEquals("Spinneys", template?.fields?.get("brand"))
        assertEquals(false, template?.fields?.containsKey("ignore"))
    }

    @Test
    fun `matching is case insensitive`() {
        val detector = RegexSmsTemplateDetector(listOf("Payment of AED {amount} to {brand} now"))

        val template = detector.detect("PAYMENT OF AED 99 TO ADNOC now")

        assertEquals("99", template?.fields?.get("amount"))
        assertEquals("ADNOC", template?.fields?.get("brand"))
    }

    @Test
    fun `real default debit template captures amount brand and date`() {
        val detector = RegexSmsTemplateDetector(DefaultSmsTemplates.patterns)

        val template = detector.detect(
            "AED 250.75 has been debited from 1234 using card 9 at Lulu on 17-06-2026 14:30.",
        )

        assertEquals("250.75", template?.fields?.get("amount"))
        assertEquals("Lulu", template?.fields?.get("brand"))
        assertEquals("17-06-2026", template?.fields?.get("date"))
    }
}
