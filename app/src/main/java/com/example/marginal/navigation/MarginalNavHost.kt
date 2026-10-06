package com.example.marginal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marginal.presentation.auth.ForgotPasswordScreen
import com.example.marginal.presentation.auth.LoginScreen
import com.example.marginal.presentation.auth.SignUpScreen
import com.example.marginal.presentation.notes.AddEditNoteScreen
import com.example.marginal.presentation.notes.AddEditNoteViewModel
import com.example.marginal.presentation.notes.NotesListScreen
import com.example.marginal.presentation.scan.ScanScreen
import com.example.marginal.presentation.settings.SettingsScreen
import com.example.marginal.presentation.splash.SplashScreen

@Composable
fun MarginalNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigateSafe("login") { popUpTo("splash") { inclusive = true } }
                },
                onNavigateToNotes = {
                    navController.navigateSafe("notes_list") { popUpTo("splash") { inclusive = true } }
                },
            )
        }
        composable("login") {
            LoginScreen(
                onForgotPasswordClick = { navController.navigateSafe("forgot_password") },
                onSignUpClick = { navController.navigateSafe("signup") },
                onLoginSuccess = {
                    navController.navigateSafe("notes_list") { popUpTo("login") { inclusive = true } }
                },
            )
        }
        composable("signup") {
            SignUpScreen(
                onBackClick = { navController.popBackStackSafe() },
                onSignUpSuccess = {
                    navController.navigateSafe("notes_list") { popUpTo("login") { inclusive = true } }
                },
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(onBackClick = { navController.popBackStackSafe() })
        }
        composable("notes_list") {
            NotesListScreen(
                onNoteClick = { noteId -> navController.navigateSafe("note_edit?noteId=$noteId") },
                onAddNoteClick = { navController.navigateSafe("note_edit") },
                onSettingsClick = { navController.navigateSafe("settings") },
            )
        }
        composable(
            route = "note_edit?noteId={noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val viewModel: AddEditNoteViewModel = hiltViewModel(backStackEntry)

            LaunchedEffect(backStackEntry) {
                backStackEntry.savedStateHandle
                    .getStateFlow<String?>("scanned_text", null)
                    .collect { text ->
                        if (text != null) {
                            viewModel.appendScannedText(text)
                            backStackEntry.savedStateHandle.remove<String>("scanned_text")
                        }
                    }
            }

            AddEditNoteScreen(
                onBackClick = { navController.popBackStackSafe() },
                onScanClick = { navController.navigateSafe("scan") },
                viewModel = viewModel,
            )
        }
        composable("scan") {
            ScanScreen(
                onBackClick = { navController.popBackStackSafe() },
                onTextRecognized = { text ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_text", text)
                    navController.popBackStackSafe()
                },
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStackSafe() },
                onSignedOut = {
                    navController.navigateSafe("login") { popUpTo("notes_list") { inclusive = true } }
                },
            )
        }
    }
}
