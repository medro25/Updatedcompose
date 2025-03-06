package com.example.composetutorial.Database



import kotlinx.coroutines.flow.Flow

class UserMessageRepository(private val userMessageDao: UserMessageDao) {
    val allMessages: Flow<List<UserMessage>> = userMessageDao.getAllMessages()

    suspend fun insertMessage(userMessage: UserMessage) {
        userMessageDao.insertMessage(userMessage)
    }
}
