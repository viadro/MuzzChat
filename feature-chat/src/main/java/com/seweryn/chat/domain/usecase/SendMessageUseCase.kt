package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.NewMessage
import com.seweryn.chat.domain.util.TimeProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

internal class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    private val timeProvider: TimeProvider,
) {

    private val trigger = MutableSharedFlow<String>()

    @OptIn(FlowPreview::class)
    suspend fun init() {
        trigger
            .conflate()
            .map {
                delay(AUTO_REPLY_DELAY)
                messagesRepository.sendMessage(
                    NewMessage(
                        isOutgoing = false,
                        text = messagesRepository.generateMessage(),
                        timestamp = timeProvider.provideCurrentTimeInMillis(),
                    )
                )
            }.collect()
    }

    suspend operator fun invoke(text: String) {
        messagesRepository.sendMessage(
            NewMessage(
                text = text,
                isOutgoing = true,
                timestamp = timeProvider.provideCurrentTimeInMillis()
            )
        )
        trigger.emit(text)
    }

    private companion object {
        const val AUTO_REPLY_DELAY = 30_000L
    }
}
