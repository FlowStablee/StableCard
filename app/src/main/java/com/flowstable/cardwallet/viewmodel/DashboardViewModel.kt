package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = false,
    val balanceMinor: Long = 0,
    val currency: String = "USD",
    val cards: List<CardEntity> = emptyList(),
    val recentTransactions: List<TransactionEntity> = emptyList(),
    val error: String? = null,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {

    val uiState: MutableState<DashboardUiState> = mutableStateOf(DashboardUiState())

    init {
        viewModelScope.launch {
            walletRepository.cards.collectLatest { cards ->
                uiState.value = uiState.value.copy(cards = cards)
            }
        }
        viewModelScope.launch {
            walletRepository.transactions.collectLatest { txs ->
                uiState.value = uiState.value.copy(
                    recentTransactions = txs.take(5)
                )
            }
        }
        refresh()
    }

    fun refresh() {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = walletRepository.refreshDashboard()
            uiState.value = if (result.isSuccess) {
                val summary = result.getOrThrow()
                uiState.value.copy(
                    isLoading = false,
                    balanceMinor = summary.balanceMinor,
                    currency = summary.currency,
                    error = null
                )
            } else {
                uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}

