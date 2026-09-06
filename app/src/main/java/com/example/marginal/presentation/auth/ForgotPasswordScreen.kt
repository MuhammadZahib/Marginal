package com.example.marginal.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.presentation.common.MarginalBackButton
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper
import com.example.marginal.ui.theme.TextMuted

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp),
    ) {
        MarginalBackButton(onClick = onBackClick, tint = Ink)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot your\npassword?",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.resetEmailSent) {
            Text(
                text = "Check your inbox — a reset link is on its way to $email.",
                color = TextMuted,
            )
        } else {
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

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = uiState.errorMessage!!, color = Brick, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.sendPasswordReset(email) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
                enabled = !uiState.isLoading && email.isNotBlank(),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp), color = Paper, strokeWidth = 2.dp)
                } else {
                    Text("Send Reset Link")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
