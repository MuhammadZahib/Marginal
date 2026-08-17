package com.example.marginal.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.presentation.auth.AuthViewModel
import com.example.marginal.presentation.common.MarginalPinIcon
import com.example.marginal.ui.theme.Amber
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToNotes: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        delay(1200)
        if (viewModel.currentUser.value != null) onNavigateToNotes() else onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MarginalPinIcon(
                modifier = Modifier.size(56.dp),
                bodyColor = Amber,
                dotColor = Ink,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Marginal", style = MaterialTheme.typography.headlineSmall, color = Paper)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "NOTES, KEPT CLOSE", style = MaterialTheme.typography.labelSmall, color = Paper.copy(alpha = 0.6f))
        }
    }
}
