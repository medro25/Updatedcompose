package com.example.composetutorial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope //    Fixed import
import com.example.composetutorial.Database.* //    Imports database files
import kotlinx.coroutines.launch

class UserMessageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserMessageRepository

    init {
        val db = AppDatabase.getDatabase(application.applicationContext) //    Fixed context usage
        val userMessageDao = db.userMessageDao()
        repository = UserMessageRepository(userMessageDao)
    }

    val allMessages = repository.allMessages

    fun insertMessage(userMessage: UserMessage) {
        viewModelScope.launch {
            repository.insertMessage(userMessage)
        }
    }
}
