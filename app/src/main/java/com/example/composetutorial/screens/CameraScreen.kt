package com.example.composetutorial.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var capturedPhotos by remember { mutableStateOf(getSavedImages(context)) }
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

    // ✅ Request Camera Permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // ✅ Check camera permission when screen loads
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasCameraPermission) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // ✅ Camera launcher that saves the image
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoPath != null) {
            capturedPhotos = getSavedImages(context) // ✅ Refresh list after capture
        }
    }

    // ✅ Function to create a file for storing the image
    fun createImageFile(context: Context): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Camera Screen") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Capture and View Photos")

            Spacer(modifier = Modifier.height(16.dp))

            if (hasCameraPermission) {
                Button(
                    onClick = {
                        val photoUri = createImageFile(context) // ✅ Generate file URI
                        takePictureLauncher.launch(photoUri) // ✅ Capture and save
                    }
                ) {
                    Text("Open Camera")
                }
            } else {
                Text("Camera permission required. Please allow in settings.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Saved Photos")

            // ✅ Ensuring LazyColumn takes remaining space and is fully scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // ✅ Allows scrolling and takes up available space
                        .fillMaxSize()
                ) {
                    items(capturedPhotos) { photoUri ->
                        Image(
                            painter = rememberAsyncImagePainter(photoUri),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ✅ Function to retrieve saved images
fun getSavedImages(context: Context): List<Uri> {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return storageDir?.listFiles()?.filter { it.extension == "jpg" }?.map {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", it)
    } ?: emptyList()
}
