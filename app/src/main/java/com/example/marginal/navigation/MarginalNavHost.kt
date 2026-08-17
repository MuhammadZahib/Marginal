package com.example.marginal.navigation

import androidx.compose.runtime.Composable
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
import com.example.marginal.presentation.notes.NotesListScreen
import com.example.marginal.presentation.splash.SplashScreen

@Composable
fun MarginalNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") { popUpTo("splash") { inclusive = true } }
                },
                onNavigateToNotes = {
                    navController.navigate("notes_list") { popUpTo("splash") { inclusive = true } }
                },
            )
        }
        composable("login") {
            LoginScreen(
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = {
                    navController.navigate("notes_list") { popUpTo("login") { inclusive = true } }
                },
            )
        }
        composable("signup") {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate("notes_list") { popUpTo("login") { inclusive = true } }
                },
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(onBackClick = { navController.popBackStack() })
        }
        composable("notes_list") {
            NotesListScreen(
                onNoteClick = { noteId -> navController.navigate("note_edit?noteId=$noteId") },
                onAddNoteClick = { navController.navigate("note_edit") },
                onSignOutClick = {
                    navController.navigate("login") { popUpTo("notes_list") { inclusive = true } }
                },
            )
        }
        composable(
            route = "note_edit?noteId={noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AddEditNoteScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
