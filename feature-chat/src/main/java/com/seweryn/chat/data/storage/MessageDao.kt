package com.seweryn.chat.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.seweryn.chat.data.storage.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun observeAll(): Flow<List<MessageEntity>>

    @Insert
    suspend fun insert(message: MessageEntity)
}