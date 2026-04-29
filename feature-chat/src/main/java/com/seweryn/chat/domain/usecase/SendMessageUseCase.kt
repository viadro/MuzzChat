package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.NewMessage

internal class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
) {

    suspend operator fun invoke(newMessage: NewMessage) {
        messagesRepository.sendMessage(newMessage)
    }
}
