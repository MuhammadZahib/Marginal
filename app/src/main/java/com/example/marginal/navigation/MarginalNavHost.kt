package com.example.marginal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marginal.presentation.auth.LoginScreen
import com.example.marginal.presentation.splash.SplashScreen

@Composable
fun MarginalNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true } // can't go back to splash
                    }
                }
            )
        }
        composable("login") {
            LoginScreen()
        }
    }
}
