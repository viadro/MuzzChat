package com.seweryn.chat.presentation

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.UserRepository
import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.domain.model.User
import com.seweryn.chat.domain.usecase.SendMessageUseCase
import com.seweryn.chat.presentation.model.ChatIntent
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.util.DateFormatter
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class ChatViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private val sendMessageUseCaseMock: SendMessageUseCase = mockk(relaxUnitFun = true)
    private val messagesFlowMock = MutableSharedFlow<List<Message>>()
    private val messagesRepositoryMock: MessagesRepository = mockk {
        every { observeMessages() } returns messagesFlowMock
    }
    private val dateFormatterMock: DateFormatter = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk {
        every { getOtherUser() } returns MOCK_USER
    }

    private lateinit var tested: ChatViewModel

    @Before
    fun setup() {
        tested = ChatViewModel(
            sendMessageUseCaseMock,
            messagesRepositoryMock,
            dateFormatterMock,
            userRepository
        )
    }

    @Test
    fun `observes chat messages on init`() = runTest {
        coVerify(exactly = 1) {
            messagesRepositoryMock.observeMessages()
        }
    }

    @Test
    fun `init - header state contains other user data`() = runTest {
        val headerState = tested.state.value.headerState
        assertEquals(MOCK_USER.name, headerState.name)
        assertEquals(MOCK_USER.image, headerState.image)
    }

    @Test
    fun `init - messages state is loading`() = runTest {
        assertEquals(ChatMessagesState.Loading, tested.state.value.messagesState)
    }

    @Test
    fun `init - initializes send message use case`() = runTest {
        coVerify(exactly = 1) {
            sendMessageUseCaseMock.init()
        }
    }

    @Test
    fun `observeMessages - updates state when messages are emitted`() = runTest {
        val messages = listOf(
            Message(id = 1, text = "Hello", isOutgoing = true, timestamp = 0L)
        )

        messagesFlowMock.emit(messages)
        testDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertTrue(tested.state.value.messagesState is ChatMessagesState.Ready)
    }

    @Test
    fun `observeMessages - empty list results in ready state with no items`() = runTest {
        messagesFlowMock.emit(emptyList())
        testDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        val messagesState = tested.state.value.messagesState
        assertTrue(messagesState is ChatMessagesState.Ready)
        assertTrue((messagesState as ChatMessagesState.Ready).items.isEmpty())
    }

    @Test
    fun `onInputValueChanged - updates input text in state`() = runTest {
        tested.processIntent(ChatIntent.InputValueChanged("Hello"))
        assertEquals("Hello", tested.state.value.inputState.text)
    }

    @Test
    fun `onInputValueChanged - enables send button when text is not blank`() = runTest {
        tested.processIntent(ChatIntent.InputValueChanged("Hello"))
        assertTrue(tested.state.value.inputState.isEnabled)
    }

    @Test
    fun `onInputValueChanged - disables send button when text is blank`() = runTest {
        tested.processIntent(ChatIntent.InputValueChanged("Hello"))
        tested.processIntent(ChatIntent.InputValueChanged(""))
        assertFalse(tested.state.value.inputState.isEnabled)
    }

    @Test
    fun `sendNewMessage - calls send message use case with current input text`() = runTest {
        val text = "Hello Sarah"
        tested.processIntent(ChatIntent.InputValueChanged(text))
        tested.processIntent(ChatIntent.SendClicked)
        testDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        coVerify { sendMessageUseCaseMock(text) }
    }

    @Test
    fun `sendNewMessage - clears input after sending`() = runTest {
        tested.processIntent(ChatIntent.InputValueChanged("Hello"))
        tested.processIntent(ChatIntent.SendClicked)
        testDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertEquals("", tested.state.value.inputState.text)
    }

    @Test
    fun `sendNewMessage - disables send button after sending`() = runTest {
        tested.processIntent(ChatIntent.InputValueChanged("Hello"))
        tested.processIntent(ChatIntent.SendClicked)
        testDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertFalse(tested.state.value.inputState.isEnabled)
    }

    private companion object {

        val MOCK_USER = User(
            name = "MockName",
            image = "MockImage",
        )
    }
}