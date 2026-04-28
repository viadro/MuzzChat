package com.seweryn.chat.domain.model

internal data class Message(
    val text: String,
    val type: MessageType,
    val timestamp: Long,
)