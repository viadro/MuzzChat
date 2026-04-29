package com.seweryn.chat.presentation

import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.presentation.model.ChatItem
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.model.ChatState
import com.seweryn.chat.presentation.model.InputState
import com.seweryn.chat.presentation.util.DateFormatter
import java.util.concurrent.TimeUnit

internal object ChatReducer {

    fun ChatState.onMessagesLoaded(
        messages: List<Message>,
        dateFormatter: DateFormatter,
    ): ChatState {
        val items = buildList {
            messages.forEachIndexed { index, message ->
                val previousMessage = messages.getOrNull(index - 1)

                val needsHeader = previousMessage == null
                        || message.timestamp - previousMessage.timestamp > TimeUnit.HOURS.toMillis(1)

                if (needsHeader) {
                    add(
                        ChatItem.DateTime(
                            key = message.timestamp,
                            value = dateFormatter.toSectionLabel(message.timestamp)
                        )
                    )
                }

                val isGrouped = previousMessage != null
                        && previousMessage.isOutgoing == message.isOutgoing
                        && message.timestamp - previousMessage.timestamp < GROUPED_MESSAGE_SPACING
                add(
                    ChatItem.Message(
                        key = message.id,
                        text = message.text,
                        isGrouped = isGrouped,
                        isOutgoing = message.isOutgoing
                    )
                )
            }
        }

        return copy(
            messagesState = ChatMessagesState.Ready(items.asReversed())
        )
    }

    fun ChatState.onInputChanged(
        newValue: String,
    ) = copy(
        inputState = InputState(
            text = newValue,
            isEnabled = newValue.isNotEmpty(),
        )
    )

    fun ChatState.onMessageSent() = copy(
        inputState = InputState(
            text = "",
        )
    )

    private const val GROUPED_MESSAGE_SPACING = 20_000L
}
