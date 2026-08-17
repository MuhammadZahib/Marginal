package com.example.marginal.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.presentation.auth.AuthViewModel
import com.example.marginal.ui.theme.Paper

// TODO: replace with the real Notes List screen (see UI kit) — this exists
// only so the auth loop (login -> here -> sign out -> login) is testable now.
@Composable
fun NotesPlaceholderScreen(
    onSignOutClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Paper),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("You're in! 🎉", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                viewModel.signOut()
                onSignOutClick()
            }) {
                Text("Sign Out")
            }
        }
    }
}
