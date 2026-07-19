package com.example.marginal.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper
import com.example.marginal.ui.theme.TextMuted

@Composable
fun ForgotPasswordScreen(onBackClick: () -> Unit) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .padding(horizontal = 26.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot your\npassword?",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter the email on your account. We'll send a link to reset your password.",
            color = TextMuted,
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* TODO: wire to AuthRepository.sendPasswordReset once Firebase is set up */ },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
        ) {
            Text("Send Reset Link")
        }
    }
}
