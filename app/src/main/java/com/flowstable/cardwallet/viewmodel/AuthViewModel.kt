package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val uiState: MutableState<AuthUiState> = mutableStateOf(AuthUiState())

    fun updateEmail(email: String) {
        uiState.value = uiState.value.copy(email = email, error = null)
    }

    fun updatePassword(password: String) {
        uiState.value = uiState.value.copy(password = password, error = null)
    }

    fun login(email: String, password: String) {
        uiState.value = uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            uiState.value = if (result.isSuccess) {
                uiState.value.copy(isLoading = false)
            } else {
                uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    fun biometricLogin() {
        // This would be triggered after successful biometric auth and token retrieval.
    }
}

