package com.seweryn.chat.domain.model

data class NewMessage(
    val text: String,
    val isOutgoing: Boolean,
    val timestamp: Long,
)