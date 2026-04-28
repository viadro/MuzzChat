package com.seweryn.chat.presentation.model

internal data class ChatState(
    val headerState: HeaderState,
    val messagesState: ChatMessagesState,
    val inputState: InputState = InputState(),
)

internal sealed interface ChatMessagesState {

    data object Loading : ChatMessagesState
    data class Ready(
        val items: List<ChatItem>,
    ) : ChatMessagesState
}

internal data class HeaderState(
    val name: String,
    val image: String,
)

internal data class InputState(
    val text: String = "",
    val isEnabled: Boolean = false,
)