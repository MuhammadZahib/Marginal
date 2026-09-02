package com.example.marginal.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.presentation.auth.AuthViewModel
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Paper
import com.example.marginal.ui.theme.Plum
import com.example.marginal.ui.theme.TextMuted

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val user by viewModel.currentUser.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding(),
    ) {
        TextButton(onClick = onBackClick, modifier = Modifier.padding(start = 12.dp)) {
            Text("← Back")
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Plum))
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = user?.displayName?.ifBlank { null } ?: "Marginal user",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = user?.email ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                )
            }
        }

        HorizontalDivider()

        SettingsGroupLabel("Account")
        SettingsClickableItem(label = "Change password", onClick = { showChangePasswordDialog = true })

        SettingsGroupLabel("Preferences")
        SettingsItem(label = "Dark mode", value = "Off")

        SettingsGroupLabel("About")
        SettingsItem(label = "App version", value = "1.0.0")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                viewModel.signOut()
                onSignedOut()
            },
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        ) {
            Text("Log out", color = Brick)
        }
    }

    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Change password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New password (min. 6 characters)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(uiState.errorMessage!!, color = Brick, style = MaterialTheme.typography.labelSmall)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.changePassword(
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            onSuccess = {
                                showChangePasswordDialog = false
                                currentPassword = ""
                                newPassword = ""
                            },
                        )
                    },
                    enabled = !uiState.isLoading && currentPassword.isNotBlank() && newPassword.length >= 6,
                ) {
                    Text(if (uiState.isLoading) "Updating…" else "Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun SettingsGroupLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
    )
}

@Composable
private fun SettingsItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

@Composable
private fun SettingsClickableItem(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text("›", color = TextMuted)
    }
}
