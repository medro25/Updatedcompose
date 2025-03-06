package com.example.composetutorial.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.composetutorial.Database.UserMessage
import com.example.composetutorial.viewmodel.UserMessageViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEntryScreen(navController: NavController, viewModel: UserMessageViewModel) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Entry") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text("Pick Profile Image")
            }

            selectedImageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (username.isNotBlank() && message.isNotBlank()) {
                        viewModel.insertMessage(
                            UserMessage(
                                username = username,
                                message = message,
                                profileImage = selectedImageUri.toString() // Store image URI
                            )
                        )
                        navController.navigate("home") // Navigate back after saving
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}
