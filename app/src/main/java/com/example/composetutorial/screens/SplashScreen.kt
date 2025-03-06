package com.example.composetutorial.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true } // Remove Splash from backstack
            }
        }, 5000) // Delay of 3 seconds
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Welcome Back! ", style = MaterialTheme.typography.headlineLarge)
    }
}
