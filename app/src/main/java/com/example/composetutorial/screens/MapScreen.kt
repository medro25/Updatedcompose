package com.example.composetutorial.screens

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MapScreen(navController: NavController) {
    val context = LocalContext.current

    // Default map position (New York City)
    var mapPosition by remember { mutableStateOf(LatLng(40.7128, -74.0060)) }
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(mapPosition, 12f)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Map Screen") })
                SearchBar(context = context) { location -> // ✅ Search Box now at the top
                    val newLatLng = getLatLngFromAddress(context, location)
                    if (newLatLng != null) {
                        mapPosition = newLatLng
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = rememberMarkerState(position = mapPosition), // ✅ Fixed Marker State
                    title = "Selected Location",
                    snippet = "Tap to select"
                )
            }
        }
    }
}

// 🔹 Updated Search Bar (Now Above the Map)
@Composable
fun SearchBar(context: Context, onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField( // ✅ Cleaner UI
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Search Location") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(searchText)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // ✅ Proper padding to separate from top bar
    )
}

// 🔹 Fixed Address to LatLng Conversion
@SuppressLint("NewApi")
fun getLatLngFromAddress(context: Context, address: String): LatLng? {
    return try {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocationName(address, 1)

        if (!addresses.isNullOrEmpty()) {
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        } else {
            Log.e("MapScreen", "No location found for: $address")
            null
        }
    } catch (e: Exception) {
        Log.e("MapScreen", "Geocoding failed: ${e.localizedMessage}")
        null
    }
}
