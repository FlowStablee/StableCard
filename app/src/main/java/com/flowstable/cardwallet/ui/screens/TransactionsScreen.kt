package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.viewmodel.TransactionsUiState

@Composable
fun TransactionsScreen(
    state: androidx.compose.runtime.State<TransactionsUiState>,
    onFilterChanged: (Long?, Long?, Long?, Long?) -> Unit,
    onBack: () -> Unit,
) {
    val ui = state.value
    val minAmount = remember { mutableStateOf("") }
    val maxAmount = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Transactions") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    // back
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = minAmount.value,
                    onValueChange = { minAmount.value = it },
                    label = { Text("Min amount") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = maxAmount.value,
                    onValueChange = { maxAmount.value = it },
                    label = { Text("Max amount") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    val min = minAmount.value.toDoubleOrNull()?.let { (it * 100).toLong() }
                    val max = maxAmount.value.toDoubleOrNull()?.let { (it * 100).toLong() }
                    onFilterChanged(null, null, min, max)
                }) {
                    Text("Apply")
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ui.transactions) { tx ->
                TransactionRow(tx)
            }
        }
    }
}

@Composable
private fun TransactionRow(tx: TransactionEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(tx.merchantName, style = MaterialTheme.typography.bodyMedium)
            Text(
                tx.status,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
        val amount = tx.amountMinor / 100.0
        Text(
            text = String.format("%s %.2f", tx.currency, amount),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

