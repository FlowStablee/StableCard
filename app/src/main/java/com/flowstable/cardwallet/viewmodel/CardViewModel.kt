package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardUiState(
    val card: CardEntity? = null,
    val spendingLimitMinor: Long? = null,
    val onlineEnabled: Boolean = true,
    val internationalEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CardViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {

    val uiState: MutableState<CardUiState> = mutableStateOf(CardUiState())

    fun observeCard(cardId: String) {
        viewModelScope.launch {
            walletRepository.getCard(cardId).collectLatest { card ->
                uiState.value = uiState.value.copy(
                    card = card,
                    spendingLimitMinor = card?.spendingLimit,
                    onlineEnabled = card?.onlineEnabled ?: true,
                    internationalEnabled = card?.internationalEnabled ?: true
                )
            }
        }
    }

    fun updateControls(
        cardId: String,
        limitMinor: Long?,
        onlineEnabled: Boolean,
        internationalEnabled: Boolean,
    ) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = walletRepository.updateControls(
                cardId,
                limitMinor,
                onlineEnabled,
                internationalEnabled
            )
            uiState.value = if (result.isSuccess) {
                uiState.value.copy(isLoading = false, error = null)
            } else {
                uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}

