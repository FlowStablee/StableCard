package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.model.TransactionFilter
import com.flowstable.cardwallet.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionsUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val filter: TransactionFilter = TransactionFilter(),
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {

    val uiState: MutableState<TransactionsUiState> = mutableStateOf(TransactionsUiState())

    init {
        viewModelScope.launch {
            walletRepository.transactions.collectLatest { txs ->
                uiState.value = uiState.value.copy(
                    transactions = applyFilterInternal(txs, uiState.value.filter)
                )
            }
        }
    }

    fun applyFilter(
        fromEpochSeconds: Long?,
        toEpochSeconds: Long?,
        minAmountMinor: Long?,
        maxAmountMinor: Long?,
    ) {
        val newFilter = TransactionFilter(
            fromEpochSeconds,
            toEpochSeconds,
            minAmountMinor,
            maxAmountMinor
        )
        val allTx = uiState.value.transactions
        uiState.value = uiState.value.copy(
            filter = newFilter,
            transactions = applyFilterInternal(allTx, newFilter)
        )
    }

    private fun applyFilterInternal(
        txs: List<TransactionEntity>,
        filter: TransactionFilter,
    ): List<TransactionEntity> {
        return txs.filter { tx ->
            val time = tx.createdAtEpochSeconds
            val amount = tx.amountMinor
            (filter.fromEpochSeconds == null || time >= filter.fromEpochSeconds) &&
                (filter.toEpochSeconds == null || time <= filter.toEpochSeconds) &&
                (filter.minAmountMinor == null || amount >= filter.minAmountMinor) &&
                (filter.maxAmountMinor == null || amount <= filter.maxAmountMinor)
        }
    }
}

