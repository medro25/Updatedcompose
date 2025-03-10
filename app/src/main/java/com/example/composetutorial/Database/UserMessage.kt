package com.example.composetutorial.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_messages")
data class UserMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val message: String,
    val profileImage: String //   Now storing absolute file path instead of URI
)
