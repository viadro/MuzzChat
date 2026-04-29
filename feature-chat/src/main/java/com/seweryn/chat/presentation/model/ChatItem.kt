package com.seweryn.chat.presentation.model

sealed interface ChatItem {

    val key: Long
    data class Message(
        override val key: Long,
        val text: String,
        val isOutgoing: Boolean,
        val isGrouped: Boolean = false,
    ) : ChatItem

    data class DateTime(
        override val key: Long,
        val value: String,
    ) : ChatItem
}

