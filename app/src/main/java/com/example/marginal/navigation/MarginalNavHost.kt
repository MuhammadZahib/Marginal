package com.example.marginal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marginal.presentation.auth.ForgotPasswordScreen
import com.example.marginal.presentation.auth.LoginScreen
import com.example.marginal.presentation.auth.SignUpScreen
import com.example.marginal.presentation.splash.SplashScreen

@Composable
fun MarginalNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onSignUpClick = { navController.navigate("signup") },
            )
        }
        composable("signup") {
            SignUpScreen(onBackClick = { navController.popBackStack() })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
