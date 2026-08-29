package com.example.marginal.presentation.settings

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding(),
    ) {
        TextButton(onClick = onBackClick, modifier = Modifier.padding(start = 12.dp)) {
            Text("← Back")
        }

        // Profile header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(Plum),
            )
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
