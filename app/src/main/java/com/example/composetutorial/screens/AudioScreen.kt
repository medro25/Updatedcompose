package com.example.composetutorial.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import java.io.File

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AudioScreen(navController: NavController) {
    val context = LocalContext.current
    var hasAudioPermission by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var recordedFiles by remember { mutableStateOf(getSavedAudioFiles(context)) } // ✅ Store saved audio files
    var currentlyPlayingFile by remember { mutableStateOf<String?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // ✅ Request audio recording permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    // ✅ Check permission on screen load
    LaunchedEffect(Unit) {
        hasAudioPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasAudioPermission) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Audio Screen") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Audio Recording")

            Spacer(modifier = Modifier.height(16.dp))

            if (hasAudioPermission) {
                Button(
                    onClick = {
                        if (isRecording) {
                            mediaRecorder?.let {
                                stopRecording(it)
                                mediaRecorder = null
                                recordedFiles = getSavedAudioFiles(context) // ✅ Refresh the file list
                            }
                            isRecording = false
                        } else {
                            mediaRecorder = startRecording(context) // ✅ Store MediaRecorder instance
                            isRecording = true
                        }
                    }
                ) {
                    Text(if (isRecording) "Stop Recording" else "Start Recording")
                }
            } else {
                Text("Permission required to record audio.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Saved Recordings")

            LazyColumn {
                items(recordedFiles) { file ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = file.name, modifier = Modifier.weight(1f))

                        Button(onClick = {
                            if (currentlyPlayingFile == file.absolutePath) {
                                mediaPlayer?.stop()
                                mediaPlayer?.release()
                                mediaPlayer = null
                                currentlyPlayingFile = null
                            } else {
                                mediaPlayer?.stop()
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer().apply {
                                    setDataSource(file.absolutePath)
                                    prepare()
                                    start()
                                }
                                currentlyPlayingFile = file.absolutePath
                            }
                        }) {
                            Text(if (currentlyPlayingFile == file.absolutePath) "Stop" else "Play")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("home") }) {
                Text("Go Back")
            }
        }
    }
}

// ✅ Function to start recording
fun startRecording(context: Context): MediaRecorder {
    val audioFile = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "recording_${System.currentTimeMillis()}.mp3")
    val mediaRecorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setOutputFile(audioFile.absolutePath)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        prepare()
        start()
    }
    return mediaRecorder
}

// ✅ Function to stop recording safely
fun stopRecording(mediaRecorder: MediaRecorder) {
    try {
        mediaRecorder.stop()
        mediaRecorder.release()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ✅ Function to get saved audio files
fun getSavedAudioFiles(context: Context): List<File> {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    return directory?.listFiles()?.filter { it.extension == "mp3" } ?: emptyList()
}
