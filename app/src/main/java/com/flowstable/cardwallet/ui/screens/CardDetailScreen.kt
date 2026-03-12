package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.ui.MainActivity
import com.flowstable.cardwallet.viewmodel.CardUiState

@Composable
fun CardDetailScreen(
    cardId: String,
    state: State<CardUiState>,
    onFreezeClick: () -> Unit,
    onBack: () -> Unit,
) {
    val ui = state.value
    val context = LocalContext.current

    DisposableEffect(Unit) {
        (context as? MainActivity)?.disableScreenshots(true)
        onDispose {
            (context as? MainActivity)?.disableScreenshots(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // back icon placeholder
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ui.card?.let { card ->
                Text(
                    text = "•••• •••• •••• ${card.last4}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Expiry", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "%02d/%02d".format(card.expiryMonth, card.expiryYear % 100),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column {
                        Text("CVV", style = MaterialTheme.typography.labelSmall)
                        Text("•••", style = MaterialTheme.typography.bodyMedium)
                    }
                    Column {
                        Text("Status", style = MaterialTheme.typography.labelSmall)
                        Text(card.status.name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onFreezeClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Freeze / controls")
                }
            } ?: run {
                Text("Loading card…")
            }
        }
    }
}

