package com.seweryn.chat.domain.model

internal data class Message(
    val text: String,
    val isOutgoing: Boolean,
    val timestamp: Long,
)