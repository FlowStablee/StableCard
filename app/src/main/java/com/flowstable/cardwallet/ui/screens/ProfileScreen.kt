package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.viewmodel.ProfileUiState

@Composable
fun ProfileScreen(
    state: State<ProfileUiState>,
    onLogout: () -> Unit,
    onBack: () -> Unit,
) {
    val ui = state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Profile") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = ui.email ?: "-", style = MaterialTheme.typography.titleMedium)
            Text(text = ui.fullName ?: "-", style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log out")
            }
        }
    }
}

