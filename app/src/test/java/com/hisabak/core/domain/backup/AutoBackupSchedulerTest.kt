package com.hisabak.core.domain.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Duration.Companion.days

class AutoBackupSchedulerTest {

    @Test
    fun `interval maps each period`() {
        assertNull(autoBackupInterval(AutoBackupPeriod.NEVER))
        assertEquals(1.days, autoBackupInterval(AutoBackupPeriod.DAILY))
        assertEquals(7.days, autoBackupInterval(AutoBackupPeriod.WEEKLY))
        assertEquals(30.days, autoBackupInterval(AutoBackupPeriod.MONTHLY))
    }
}
