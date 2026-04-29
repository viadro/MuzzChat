package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.NewMessage
import com.seweryn.chat.domain.util.TimeProvider

internal class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    private val timeProvider: TimeProvider,
) {

    suspend operator fun invoke(text: String) {
        messagesRepository.sendMessage(
            NewMessage(
                text = text,
                isOutgoing = true,
                timestamp = timeProvider.provideCurrentTimeInMillis()
            )
        )
    }
}
