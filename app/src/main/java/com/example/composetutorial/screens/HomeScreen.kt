package com.example.composetutorial.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.composetutorial.viewmodel.UserMessageViewModel
import java.io.File

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavController, viewModel: UserMessageViewModel) {
    val messages by viewModel.allMessages.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Home Screen") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Messages")

            LazyColumn {
                items(messages) { message ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val file = File(message.profileImage) // 🔥 Retrieve image from file

                            if (file.exists()) {
                                Image(
                                    painter = rememberAsyncImagePainter(file),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.size(50.dp)
                                )
                            } else {
                                Text("Image not found") // 🔥 Show placeholder if file is missing
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = message.username, style = MaterialTheme.typography.bodyLarge)
                                Text(text = message.message, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
