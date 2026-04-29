package com.seweryn.chat.data.storage.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.domain.model.NewMessage

@Entity(tableName = "messages")
internal data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isOutgoing: Boolean,
    val text: String,
    val timestamp: Long
)

internal fun MessageEntity.toDomain(): Message =
    Message(
        id = id,
        isOutgoing = isOutgoing,
        text = text,
        timestamp = timestamp,
    )

internal fun NewMessage.toEntity(): MessageEntity =
    MessageEntity(
        isOutgoing = isOutgoing,
        text = text,
        timestamp = timestamp,
    )
