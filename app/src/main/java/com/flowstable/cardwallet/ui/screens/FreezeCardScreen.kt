package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.viewmodel.CardUiState

@Composable
fun FreezeCardScreen(
    cardId: String,
    state: androidx.compose.runtime.State<CardUiState>,
    onConfirm: (Long?, Boolean, Boolean) -> Unit,
    onBack: () -> Unit,
) {
    val ui = state.value
    val limitState = remember { mutableStateOf(ui.spendingLimitMinor?.toString().orEmpty()) }
    val online = remember { mutableStateOf(ui.onlineEnabled) }
    val international = remember { mutableStateOf(ui.internationalEnabled) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Card controls") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    // back
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Text("Spending limit (optional)", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = limitState.value,
            onValueChange = { limitState.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. 1000.00") }
        )

        RowWithSwitch(
            title = "Online payments",
            checked = online.value,
            onCheckedChange = { online.value = it }
        )

        RowWithSwitch(
            title = "International payments",
            checked = international.value,
            onCheckedChange = { international.value = it }
        )

        Button(
            onClick = {
                val limit = limitState.value.toDoubleOrNull()?.let { (it * 100).toLong() }
                onConfirm(limit, online.value, international.value)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save controls")
        }
    }
}

@Composable
private fun RowWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

