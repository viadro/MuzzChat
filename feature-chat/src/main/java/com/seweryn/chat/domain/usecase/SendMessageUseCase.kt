package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.NewMessage
import com.seweryn.chat.domain.util.TimeProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

internal class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    private val timeProvider: TimeProvider,
) {

    private val trigger = MutableSharedFlow<Unit>()

    @OptIn(FlowPreview::class)
    suspend fun init() {
        trigger
            .debounce(WAIT_TIME_UNTIL_AUTO_REPLY)
            .map {
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
        trigger.emit(Unit)
    }

    private companion object {
        const val WAIT_TIME_UNTIL_AUTO_REPLY = 2_000L
    }
}
