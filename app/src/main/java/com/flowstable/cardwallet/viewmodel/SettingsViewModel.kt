package com.flowstable.cardwallet.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val darkMode: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {

    val uiState: MutableState<SettingsUiState> = mutableStateOf(SettingsUiState())

    fun toggleDarkMode() {
        uiState.value = uiState.value.copy(darkMode = !uiState.value.darkMode)
        // Persist to DataStore in a real app
    }
}

