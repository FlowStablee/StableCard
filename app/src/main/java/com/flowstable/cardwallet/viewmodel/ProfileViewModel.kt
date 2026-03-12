package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowstable.cardwallet.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val email: String? = null,
    val fullName: String? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val uiState: MutableState<ProfileUiState> = mutableStateOf(ProfileUiState())

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

