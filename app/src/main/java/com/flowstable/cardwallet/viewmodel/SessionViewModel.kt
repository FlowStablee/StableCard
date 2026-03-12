package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.security.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class SessionState(
    val isAuthenticated: Boolean = false,
    val checkedInitialSession: Boolean = false,
    val lastInteractionEpochSeconds: Long = Instant.now().epochSecond,
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenStore: TokenStore,
) : ViewModel() {

    val sessionState: MutableState<SessionState> = mutableStateOf(SessionState())

    init {
        viewModelScope.launch {
            val token = tokenStore.getAccessToken()
            sessionState.value = sessionState.value.copy(
                isAuthenticated = !token.isNullOrBlank(),
                checkedInitialSession = true,
            )
        }
    }

    fun markInteraction() {
        sessionState.value = sessionState.value.copy(
            lastInteractionEpochSeconds = Instant.now().epochSecond
        )
    }

    fun setAuthenticated(authenticated: Boolean) {
        sessionState.value = sessionState.value.copy(isAuthenticated = authenticated)
    }
}

