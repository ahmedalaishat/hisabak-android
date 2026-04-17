package com.hisabak.feature.transaction.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.common.Currency
import com.hisabak.core.common.DomainResult
import com.hisabak.core.common.Money
import com.hisabak.feature.brand.domain.BrandRepository
import com.hisabak.feature.brand.domain.usecase.FindOrCreateBrandUseCase
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.feature.transaction.domain.TransactionRepository
import com.hisabak.feature.transaction.domain.usecase.CreateTransactionUseCase
import com.hisabak.feature.transaction.domain.usecase.UpdateTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionEditViewModel(
    private val transactionId: TransactionId?,
    private val currency: Currency,
    private val transactionRepository: TransactionRepository,
    private val brandRepository: BrandRepository,
    observeBrands: ObserveBrandsUseCase,
    private val findOrCreateBrand: FindOrCreateBrandUseCase,
    private val createTransaction: CreateTransactionUseCase,
    private val updateTransaction: UpdateTransactionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionEditUiState(isNew = transactionId == null))
    val state: StateFlow<TransactionEditUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeBrands().collect { brands ->
                _state.update { it.copy(brandSuggestions = brands.map { b -> b.name }) }
            }
        }
        if (transactionId != null) loadExisting(transactionId)
    }

    private fun loadExisting(id: TransactionId) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = transactionRepository.getById(id)) {
                is DomainResult.Success -> {
                    val tx = result.value
                    val brand = (brandRepository.getById(tx.brandId) as? DomainResult.Success)?.value
                    _state.update {
                        it.copy(
                            isLoading = false,
                            amountInput = formatAmountInput(tx.amount),
                            brandInput = brand?.name.orEmpty(),
                            noteInput = tx.note.orEmpty(),
                        )
                    }
                }
                is DomainResult.Failure -> _state.update {
                    it.copy(isLoading = false, generalError = result.error.message)
                }
            }
        }
    }

    fun onAmountChange(value: String) {
        _state.update { it.copy(amountInput = value, amountError = null) }
    }

    fun onBrandChange(value: String) {
        _state.update { it.copy(brandInput = value, brandError = null) }
    }

    fun onNoteChange(value: String) {
        _state.update { it.copy(noteInput = value) }
    }

    fun save() {
        val s = _state.value
        val minor = parseAmountMinor(s.amountInput)
        if (minor == null || minor <= 0) {
            _state.update { it.copy(amountError = "Enter a positive amount") }
            return
        }
        val brandName = s.brandInput.trim()
        if (brandName.isEmpty()) {
            _state.update { it.copy(brandError = "Brand is required") }
            return
        }
        _state.update { it.copy(isSaving = true, generalError = null) }
        viewModelScope.launch {
            val brandResult = findOrCreateBrand(brandName)
            if (brandResult is DomainResult.Failure) {
                _state.update { it.copy(isSaving = false, brandError = brandResult.error.message) }
                return@launch
            }
            val brand = (brandResult as DomainResult.Success).value
            val money = Money(minor, currency)
            val note = s.noteInput.trim().ifEmpty { null }

            val result = if (transactionId == null) {
                createTransaction(amount = money, brandId = brand.id, note = note).map { }
            } else {
                when (val existing = transactionRepository.getById(transactionId)) {
                    is DomainResult.Success -> updateTransaction(
                        existing.value.copy(amount = money, brandId = brand.id, note = note),
                    )
                    is DomainResult.Failure -> DomainResult.Failure(existing.error)
                }
            }

            when (result) {
                is DomainResult.Success -> _state.update { it.copy(isSaving = false, saved = true) }
                is DomainResult.Failure -> _state.update {
                    it.copy(isSaving = false, generalError = result.error.message)
                }
            }
        }
    }

}

private fun parseAmountMinor(input: String): Long? {
    val trimmed = input.trim().replace(",", "")
    if (trimmed.isEmpty()) return null
    val value = trimmed.toDoubleOrNull() ?: return null
    return (value * 100).toLong()
}

private fun formatAmountInput(money: Money): String {
    val whole = money.amountMinor / 100
    val frac = kotlin.math.abs(money.amountMinor % 100)
    return "$whole.${frac.toString().padStart(2, '0')}"
}

