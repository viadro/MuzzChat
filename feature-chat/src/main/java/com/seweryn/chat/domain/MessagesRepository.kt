package com.seweryn.chat.domain

import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.domain.model.NewMessage
import kotlinx.coroutines.flow.Flow

internal interface MessagesRepository {

    fun observeMessages(): Flow<List<Message>>

    suspend fun sendMessage(message: NewMessage)

    suspend fun generateMessage(): String
}
