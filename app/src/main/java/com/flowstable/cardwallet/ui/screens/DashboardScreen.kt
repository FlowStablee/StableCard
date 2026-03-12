package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.viewmodel.DashboardUiState

@Composable
fun DashboardScreen(
    state: State<DashboardUiState>,
    onCardSelected: (String) -> Unit,
    onViewTransactions: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val ui = state.value

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Flowstable Wallet") },
            actions = {
                IconButton(onClick = onProfileClick) {
                    // Placeholder icon
                }
                IconButton(onClick = onSettingsClick) {
                    // Placeholder icon
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceCard(balanceMinor = ui.balanceMinor, currency = ui.currency)
            }
            item {
                Text(
                    text = "Your cards",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            items(ui.cards) { card ->
                CardItem(card = card, onClick = { onCardSelected(card.id) })
            }
            item {
                Text(
                    text = "Recent transactions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            items(ui.recentTransactions) { tx ->
                TransactionRow(tx)
            }
        }
    }
}

@Composable
private fun BalanceCard(balanceMinor: Long, currency: String) {
    val displayAmount = balanceMinor / 100.0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "Wallet balance",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = String.format("%s %.2f", currency, displayAmount),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CardItem(card: CardEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = card.brand,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "•••• •••• •••• ${card.last4}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(card.holderName, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "%02d/%02d".format(card.expiryMonth, card.expiryYear % 100),
                    style = MaterialTheme.typography.bodySmall
                )
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
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

