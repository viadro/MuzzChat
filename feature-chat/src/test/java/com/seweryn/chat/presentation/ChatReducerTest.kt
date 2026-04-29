package com.seweryn.chat.presentation

import com.seweryn.chat.domain.model.Message
import com.seweryn.chat.presentation.ChatReducer.onInputChanged
import com.seweryn.chat.presentation.ChatReducer.onMessageSent
import com.seweryn.chat.presentation.ChatReducer.onMessagesLoaded
import com.seweryn.chat.presentation.model.ChatItem
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.model.ChatState
import com.seweryn.chat.presentation.model.HeaderState
import com.seweryn.chat.presentation.model.InputState
import com.seweryn.chat.presentation.util.DateFormatter
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class ChatReducerTest {

    private val dateFormatterMock = mockk<DateFormatter> {
        every { toSectionLabel(any()) } returns MOCK_FORMATTED_DATE
    }

    private fun initialState() = ChatState(
        headerState = HeaderState(name = "Sarah", image = "Image"),
        messagesState = ChatMessagesState.Loading,
        inputState = InputState(text = "", isEnabled = false)
    )

    // region onInputChanged

    @Test
    fun `onInputChanged - updates text`() {
        val result = initialState().onInputChanged(MOCK_INPUT)
        assertEquals(MOCK_INPUT, result.inputState.text)
    }

    @Test
    fun `onInputChanged - enables button when text is not empty`() {
        val result = initialState().onInputChanged(MOCK_INPUT)
        assertTrue(result.inputState.isEnabled)
    }

    @Test
    fun `onInputChanged - disables button when text is empty`() {
        val result = initialState().onInputChanged("")
        assertFalse(result.inputState.isEnabled)
    }

    @Test
    fun `onMessageSent - clears input text`() {
        val state = initialState().onInputChanged(MOCK_INPUT)
        val result = state.onMessageSent()
        assertEquals("", result.inputState.text)
    }

    @Test
    fun `onMessageSent - disables send button`() {
        val state = initialState().onInputChanged(MOCK_INPUT)
        val result = state.onMessageSent()
        assertFalse(result.inputState.isEnabled)
    }

    @Test
    fun `onMessagesLoaded - empty list results in empty ready state`() {
        val result = initialState().onMessagesLoaded(emptyList(), dateFormatterMock)
        assertTrue(result.messagesState is ChatMessagesState.Ready)
        assertTrue((result.messagesState as ChatMessagesState.Ready).items.isEmpty())
    }

    @Test
    fun `onMessagesLoaded - single message adds date header`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L)
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val items = result.readyItems()

        assertTrue(items.any { it is ChatItem.DateTime })
    }

    @Test
    fun `onMessagesLoaded - adds date header when gap is more than one hour`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L),
            message(id = 2, timestamp = TimeUnit.MINUTES.toMillis(61))
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val items = result.readyItems()

        assertEquals(2, items.filterIsInstance<ChatItem.DateTime>().size)
    }

    @Test
    fun `onMessagesLoaded - no header when gap is less than one hour`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L),
            message(id = 2, timestamp = TimeUnit.MINUTES.toMillis(59))
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val items = result.readyItems()

        assertEquals(1, items.filterIsInstance<ChatItem.DateTime>().size)
    }

    @Test
    fun `onMessagesLoaded - messages are grouped when same sender within 20 seconds`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L, isOutgoing = true),
            message(id = 2, timestamp = 10_000L, isOutgoing = true)
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertTrue(messageItems.first().isGrouped)
    }

    @Test
    fun `onMessagesLoaded - messages not grouped when different sender`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L, isOutgoing = true),
            message(id = 2, timestamp = 10_000L, isOutgoing = false)
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertFalse(messageItems.first().isGrouped)
    }

    @Test
    fun `onMessagesLoaded - messages not grouped when gap is more than 20 seconds`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L, isOutgoing = true),
            message(id = 2, timestamp = 21_000L, isOutgoing = true)
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertFalse(messageItems.first().isGrouped)
    }

    @Test
    fun `onMessagesLoaded - outgoing message has correct type`() {
        val messages = listOf(message(id = 1, isOutgoing = true))
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertTrue(messageItems.first().isOutgoing)
    }

    @Test
    fun `onMessagesLoaded - incoming message has correct type`() {
        val messages = listOf(message(id = 1, isOutgoing = false))
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertFalse(messageItems.first().isOutgoing)
    }

    @Test
    fun `onMessagesLoaded - date header uses formatted timestamp`() {
        every { dateFormatterMock.toSectionLabel(0L) } returns MOCK_FORMATTED_DATE
        val messages = listOf(message(id = 1, timestamp = 0L))
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val header = result.readyItems().filterIsInstance<ChatItem.DateTime>().first()

        assertEquals(MOCK_FORMATTED_DATE, header.value)
    }

    @Test
    fun `onMessagesLoaded - newest message is at bottom of list`() {
        val messages = listOf(
            message(id = 1, timestamp = 0L),
            message(id = 2, timestamp = 1_000L)
        )
        val result = initialState().onMessagesLoaded(messages, dateFormatterMock)
        val messageItems = result.readyItems().filterIsInstance<ChatItem.Message>()

        assertEquals(1L, messageItems.last().key)
    }

    // endregion

    // region helpers

    private fun message(
        id: Long = 1L,
        text: String = MOCK_INPUT,
        isOutgoing: Boolean = true,
        timestamp: Long = 0L
    ) = Message(
        id = id,
        text = text,
        isOutgoing = isOutgoing,
        timestamp = timestamp
    )

    private fun ChatState.readyItems() =
        (messagesState as ChatMessagesState.Ready).items

    companion object {

        const val MOCK_FORMATTED_DATE = "Thursday 11:59"
        const val MOCK_INPUT = "Hello"
    }
}