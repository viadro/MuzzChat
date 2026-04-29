package com.seweryn.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.UserRepository
import com.seweryn.chat.domain.usecase.SendMessageUseCase
import com.seweryn.chat.presentation.ChatReducer.onInputChanged
import com.seweryn.chat.presentation.ChatReducer.onMessageSent
import com.seweryn.chat.presentation.ChatReducer.onMessagesLoaded
import com.seweryn.chat.presentation.model.ChatIntent
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.model.ChatState
import com.seweryn.chat.presentation.model.HeaderState
import com.seweryn.chat.presentation.util.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val messagesRepository: MessagesRepository,
    private val dateFormatter: DateFormatter,
    userRepository: UserRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            messagesRepository.observeMessages().collect { messages ->
                _state.update { it.onMessagesLoaded(messages, dateFormatter) }
            }
        }
    }

    private val _state = MutableStateFlow(
        ChatState(
            headerState = userRepository.getOtherUser().let { user ->
                HeaderState(user.name, user.image)
            },
            messagesState = ChatMessagesState.Loading,
        )
    )

    val state: StateFlow<ChatState> = _state

    fun processIntent(chatIntent: ChatIntent) {
        when (chatIntent) {
            ChatIntent.SendClicked -> sendNewMessage()
            is ChatIntent.InputValueChanged -> onInputValueChanged(chatIntent.text)
        }
    }

    private fun onInputValueChanged(newValue: String) {
        _state.update { it.onInputChanged(newValue) }
    }

    private fun sendNewMessage() {
        viewModelScope.launch {
            sendMessageUseCase(state.value.inputState.text)
            _state.update { it.onMessageSent() }
        }
    }
}
