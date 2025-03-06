package com.example.composetutorial.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEntryScreen(navController: NavController, viewModel: UserMessageViewModel) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImagePath by remember { mutableStateOf<String?>(null) } // 🔥 Added to store the absolute file path

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        selectedImagePath = uri?.let { saveImageToInternalStorage(context, it) } // 🔥 Store the absolute file path
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
                    if (username.isNotBlank() && message.isNotBlank() && selectedImagePath != null) {
                        viewModel.insertMessage(
                            UserMessage(
                                username = username,
                                message = message,
                                profileImage = selectedImagePath!! // 🔥 Now storing absolute file path instead of URI
                            )
                        )
                        navController.navigate("home")
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}

// 🔥 Function to Save Image to Internal Storage and Return Absolute Path
fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val fileName = getFileName(context, uri) ?: return null
    val file = File(context.filesDir, fileName) // 🔥 Save image in app's internal storage

    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file.absolutePath // 🔥 Return absolute path to be stored in database
}

// 🔥 Function to Get File Name from URI
fun getFileName(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        it.moveToFirst()
        it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
    }
}
