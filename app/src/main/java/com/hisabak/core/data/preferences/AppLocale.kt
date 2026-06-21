package com.hisabak.core.data.preferences

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Per-app language, applied without AppCompat: the chosen BCP-47 tag is stored in a small
 * synchronous SharedPreferences (so it can be read in [android.app.Activity.attachBaseContext]
 * before any resources are resolved), and [wrap] rebuilds the Context with that locale + layout
 * direction. Empty tag = follow the system locale.
 */
object AppLocale {
    const val ENGLISH = "en"
    const val ARABIC = "ar"

    private const val PREFS = "hisabak_locale"
    private const val KEY = "language"

    fun getLanguageTag(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "").orEmpty()

    fun setLanguageTag(context: Context, tag: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, tag).apply()
    }

    /** Wraps [base] so its resources resolve in the saved language; no-op when none is set. */
    fun wrap(base: Context): Context {
        val tag = getLanguageTag(base)
        if (tag.isEmpty()) return base
        val locale = Locale.forLanguageTag(tag)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return base.createConfigurationContext(config)
    }
}
