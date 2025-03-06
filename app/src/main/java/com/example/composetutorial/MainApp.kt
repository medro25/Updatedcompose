package com.example.composetutorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build // 🔥 Added to check API version
import android.app.Application // 🔥 Added to initialize ViewModel manually if needed
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel // 🔥 Added to retrieve ViewModel without DI
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.composetutorial.screens.*
import com.example.composetutorial.viewmodel.UserMessageViewModel // 🔥 Import ViewModel
import androidx.activity.compose.rememberLauncherForActivityResult // 🔥 Added for runtime permission request
import androidx.activity.result.contract.ActivityResultContracts // 🔥 Added for permission handling

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ✅ Initialize LightSensorManager
    val lightSensorManager = remember { LightSensorManager(context) }

    // 🔥 Request Notification Permission for Android 13+
    val requestNotificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                println("❌ Notification permission denied by user!")
            } else {
                println("✅ Notification permission granted!")
            }
        }
    )

    // 🔥 Ask for notification permission if required
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // ✅ Register Sensor Listener when App Starts
    LaunchedEffect(Unit) {
        lightSensorManager.registerListener()
    }

    // ✅ Unregister Sensor Listener when App is Destroyed
    DisposableEffect(Unit) {
        onDispose {
            lightSensorManager.unregisterListener()
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // 🔥 Always show BottomNavigationBar
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash", // ✅ Splash screen is the first screen
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("permission") { PermissionScreen(navController) }
            composable("camera") { CameraScreen(navController) }

            composable("home") {
                val viewModel: UserMessageViewModel = viewModel() // 🔥 Initialize ViewModel
                HomeScreen(navController, viewModel) // 🔥 Pass ViewModel
            }

            composable("add_entry") {
                val viewModel: UserMessageViewModel = viewModel() // 🔥 Initialize ViewModel
                AddEntryScreen(navController, viewModel) // 🔥 Pass ViewModel
            }

            // ✅ Check permission before navigating to Map
            composable("map") {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    MapScreen(navController)
                } else {
                    PermissionScreen(navController)
                }
            }

            // ✅ Check permission before navigating to Audio
            composable("audio") {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    AudioScreen(navController)
                } else {
                    PermissionScreen(navController)
                }
            }
        }
    }
}
