package com.hisabak.feature.transaction.presentation.edit

data class TransactionEditUiState(
    val amountInput: String = "",
    val brandInput: String = "",
    val noteInput: String = "",
    val brandSuggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val amountError: String? = null,
    val brandError: String? = null,
    val generalError: String? = null,
) {
    val canSave: Boolean
        get() = !isSaving && amountInput.isNotBlank() && brandInput.isNotBlank()
}
