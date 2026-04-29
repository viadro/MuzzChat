package com.seweryn.chat.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seweryn.chat.data.storage.model.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
internal abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}