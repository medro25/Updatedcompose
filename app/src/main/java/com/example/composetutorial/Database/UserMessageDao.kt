package com.example.composetutorial.Database



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(userMessage: UserMessage)

    @Query("SELECT * FROM user_messages ORDER BY id DESC")
    fun getAllMessages(): Flow<List<UserMessage>> // Live data
}
