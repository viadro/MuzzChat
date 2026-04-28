package com.seweryn.chat.presentation.model

internal data class ChatState(
    val headerState: HeaderState,
    val messagesState: ChatMessagesState,
)

internal sealed interface ChatMessagesState {

    data object Loading : ChatMessagesState
    data class Ready(
        val items: List<ChatItem>,
    ) : ChatMessagesState
}

data class HeaderState(
    val name: String,
    val image: String,
)