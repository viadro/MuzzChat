package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.model.NewMessage
import com.seweryn.chat.domain.util.TimeProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMessageUseCaseTest {

    private val messagesRepositoryMock: MessagesRepository = mockk(relaxUnitFun = true) {
        coEvery { generateMessage() } returns "Auto reply"
    }
    private val timeProviderMock: TimeProvider = mockk {
        every { provideCurrentTimeInMillis() } returns MOCK_TIME
    }

    private lateinit var tested: SendMessageUseCase

    @Before
    fun setup() {
        tested = SendMessageUseCase(messagesRepositoryMock, timeProviderMock)
    }

    @Test
    fun `invoke - sends message with correct text`() = runTest {
        tested(MOCK_MESSAGE_TEXT)

        coVerify {
            messagesRepositoryMock.sendMessage(
                NewMessage(
                    text = MOCK_MESSAGE_TEXT,
                    isOutgoing = true,
                    timestamp = MOCK_TIME
                )
            )
        }
    }

    @Test
    fun `invoke - sends message with current timestamp`() = runTest {
        tested(MOCK_MESSAGE_TEXT)

        coVerify {
            messagesRepositoryMock.sendMessage(
                match { it.timestamp == MOCK_TIME }
            )
        }
    }

    @Test
    fun `invoke - sends outgoing message`() = runTest {
        tested(MOCK_MESSAGE_TEXT)

        coVerify {
            messagesRepositoryMock.sendMessage(
                match { it.isOutgoing }
            )
        }
    }

    @Test
    fun `init - sends auto reply after delay`() = runTest {
        val job = launch { tested.init() }
        advanceTimeBy(1L)

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(2_001L)

        coVerify {
            messagesRepositoryMock.sendMessage(
                match { !it.isOutgoing }
            )
        }

        job.cancel()
    }

    @Test
    fun `init - auto reply uses generated message`() = runTest {
        val generatedMessage = "Nice!"
        coEvery { messagesRepositoryMock.generateMessage() } returns generatedMessage

        val job = launch { tested.init() }
        advanceTimeBy(1L)

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(2_001L)

        coVerify {
            messagesRepositoryMock.sendMessage(
                NewMessage(
                    text = generatedMessage,
                    isOutgoing = false,
                    timestamp = MOCK_TIME
                )
            )
        }

        job.cancel()
    }

    @Test
    fun `init - auto reply has current timestamp`() = runTest {
        val job = launch { tested.init() }
        advanceTimeBy(1L)

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(2_001L)

        coVerify {
            messagesRepositoryMock.sendMessage(
                match { !it.isOutgoing && it.timestamp == MOCK_TIME }
            )
        }
        job.cancel()
    }

    @Test
    fun `init - no auto reply before delay`() = runTest {
        val job = launch { tested.init() }

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(1_999L)

        coVerify(exactly = 0) {
            messagesRepositoryMock.sendMessage(
                match { !it.isOutgoing }
            )
        }

        job.cancel()
    }

    @Test
    fun `init - debounce sends single reply for multiple messages`() = runTest {
        val job = launch { tested.init() }

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(500L)
        tested("How are you?")
        advanceTimeBy(500L)
        tested("What's up?")
        advanceTimeBy(2_001L)

        coVerify(exactly = 1) {
            messagesRepositoryMock.sendMessage(
                match { !it.isOutgoing }
            )
        }

        job.cancel()
    }

    @Test
    fun `init - sends reply for each burst of messages`() = runTest {
        val job = launch { tested.init() }
        advanceTimeBy(1L)

        tested(MOCK_MESSAGE_TEXT)
        advanceTimeBy(2_001L)

        tested("How are you?")
        advanceTimeBy(2_001L)

        coVerify(exactly = 2) {
            messagesRepositoryMock.sendMessage(
                match { !it.isOutgoing }
            )
        }
        job.cancel()
    }

    private companion object {

        const val MOCK_TIME = 1234567890L
        const val MOCK_MESSAGE_TEXT = "Hello"
    }
}