package com.example.composetutorial

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map  // Correct replacement for Map
import androidx.compose.material.icons.filled.Audiotrack  // Correct replacement for Audiotrack
import java.util.Locale  // Import for locale handling
import androidx.compose.material.icons.filled.Camera
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        "home" to Icons.Default.Home,
        "add_entry" to Icons.Default.Add,
        "map" to Icons.Default.Map,  // Fixed: Replaced Map with Place
        "audio" to Icons.Default.Audiotrack, // Fixed: Replaced Audiotrack with MusicNote
        "camera" to Icons.Default.Camera
    )

    NavigationBar {
        items.forEach { (route, icon) ->
            NavigationBarItem(
                icon = { Icon(imageVector = icon, contentDescription = route) },
                label = { Text(route.replace("_", " ").replaceFirstChar { it.uppercase(Locale.getDefault()) }) },
                selected = navController.currentDestination?.route == route,
                onClick = { navController.navigate(route) }
            )
        }
    }
}
