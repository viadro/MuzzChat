package com.seweryn.chat.domain

internal data class Message(
    val text: String,
    val type: MessageType,
    val timestamp: Long,
)