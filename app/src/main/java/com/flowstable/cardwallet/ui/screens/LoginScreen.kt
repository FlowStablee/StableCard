package com.flowstable.cardwallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.flowstable.cardwallet.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    state: androidx.compose.runtime.State<AuthUiState>,
    onLogin: (String, String) -> Unit,
    onBiometricLogin: () -> Unit,
) {
    val ui = state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Flowstable Card Wallet",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Securely manage your virtual and physical cards.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = ui.email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = ui.password,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            if (ui.error != null) {
                Text(
                    text = ui.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { onLogin(ui.email, ui.password) },
                enabled = !ui.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(if (ui.isLoading) "Signing in..." else "Sign in")
            }

            OutlinedButton(
                onClick = onBiometricLogin,
                enabled = !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign in with biometrics")
            }

            TextButton(
                onClick = { /* TODO: forgot password */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Forgot password?")
            }
        }
    }
}

