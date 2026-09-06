package com.example.marginal.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper

@Composable
fun LoginScreen(
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Sign in to\nyour notes",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Spacer(modifier = Modifier.width(1.dp).weight(1f))
            TextButton(onClick = onForgotPasswordClick) {
                Text("Forgot password?")
            }
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = uiState.errorMessage!!, color = Brick, style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.signIn(email, password, onSuccess = onLoginSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
            enabled = !uiState.isLoading && email.isNotBlank() && password.isNotBlank(),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp), color = Paper, strokeWidth = 2.dp)
            } else {
                Text("Sign In")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onSignUpClick) {
            Text("New here? Create an account")
        }
    }
}
