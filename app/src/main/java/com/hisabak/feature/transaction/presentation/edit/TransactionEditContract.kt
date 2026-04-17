package com.hisabak.feature.transaction.presentation.edit

import com.hisabak.core.presentation.ViewEffect
import com.hisabak.core.presentation.ViewIntent
import com.hisabak.core.presentation.ViewState

data class TransactionEditUiState(
    val amountInput: String = "",
    val brandInput: String = "",
    val noteInput: String = "",
    val brandSuggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isNew: Boolean = true,
    val amountError: String? = null,
    val brandError: String? = null,
    val generalError: String? = null,
) : ViewState {
    val canSave: Boolean
        get() = !isSaving && amountInput.isNotBlank() && brandInput.isNotBlank()
}

sealed interface TransactionEditIntent : ViewIntent {
    data class AmountChanged(val value: String) : TransactionEditIntent
    data class BrandChanged(val value: String) : TransactionEditIntent
    data class NoteChanged(val value: String) : TransactionEditIntent
    data object Save : TransactionEditIntent
}

sealed interface TransactionEditEffect : ViewEffect {
    data object Saved : TransactionEditEffect
}
