package com.seweryn.chat.data

import com.seweryn.chat.data.storage.MessageDao
import com.seweryn.chat.data.storage.model.toDomain
import com.seweryn.chat.data.storage.model.toEntity
import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.domain.model.NewMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MessagesRepositoryImpl(
    private val messageDao: MessageDao,
) : MessagesRepository {

    override fun observeMessages(): Flow<List<Message>> =
        messageDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun sendMessage(message: NewMessage) {
        messageDao.insert(message.toEntity())
    }
}