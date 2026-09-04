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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.presentation.common.BackArrowIcon
import com.example.marginal.presentation.common.MarginalIconButton
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .padding(horizontal = 26.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        MarginalIconButton(onClick = onBackClick) {
            BackArrowIcon(modifier = Modifier.size(20.dp), tint = Ink)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your\naccount",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(16.dp))

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
            label = { Text("Password (min. 6 characters)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreedToTerms, onCheckedChange = { agreedToTerms = it })
            Text("I agree to the Terms & Privacy Policy", style = MaterialTheme.typography.labelSmall)
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = uiState.errorMessage!!, color = Brick, style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.signUp(name, email, password, onSuccess = onSignUpSuccess) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
            enabled = !uiState.isLoading && agreedToTerms && name.isNotBlank() && email.isNotBlank() && password.length >= 6,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp), color = Paper, strokeWidth = 2.dp)
            } else {
                Text("Create Account")
            }
        }
    }
}
