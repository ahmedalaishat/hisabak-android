package com.hisabak.feature.budget.domain.usecase

import com.hisabak.feature.budget.domain.Budget
import com.hisabak.feature.budget.domain.BudgetRepository
import com.hisabak.core.common.Clock
import com.hisabak.core.common.DomainResult

class UpdateBudgetUseCase(
    private val repository: BudgetRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(budget: Budget): DomainResult<Unit> {
        val updated = budget.copy(
            sync = budget.sync.copy(updatedAt = clock.now(), isDirty = true),
        )
        return repository.upsert(updated)
    }
}
