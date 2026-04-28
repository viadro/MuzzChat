package com.seweryn.chat.presentation

import androidx.lifecycle.ViewModel
import com.seweryn.chat.domain.UserRepository
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.model.ChatState
import com.seweryn.chat.presentation.model.HeaderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class ChatViewModel(
    userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        ChatState(
            headerState = userRepository.getOtherUser().let { user ->
                HeaderState(user.name, user.image)
            },
            messagesState = ChatMessagesState.Loading,
        )
    )

    val state: StateFlow<ChatState> = _state
}
