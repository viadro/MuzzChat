package com.seweryn.chat.presentation.model

sealed interface ChatItem {

    data class Message(
        val text: String,
        val isOutgoing: Boolean,
        val isGrouped: Boolean = false,
    ) : ChatItem

    data class DateTime(val value: String) : ChatItem
}

