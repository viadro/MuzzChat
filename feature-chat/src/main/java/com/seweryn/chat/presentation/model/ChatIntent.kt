package com.seweryn.chat.presentation.model

internal sealed interface ChatIntent {

    data class InputValueChanged(val text: String) : ChatIntent
    data object SendClicked : ChatIntent
}
