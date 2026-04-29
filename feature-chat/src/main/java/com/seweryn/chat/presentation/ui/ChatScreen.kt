package com.seweryn.chat.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seweryn.chat.presentation.ChatViewModel
import com.seweryn.chat.presentation.model.ChatIntent
import com.seweryn.chat.presentation.model.ChatItem
import com.seweryn.chat.presentation.model.ChatMessagesState
import com.seweryn.chat.presentation.model.ChatState
import com.seweryn.chat.presentation.model.HeaderState
import com.seweryn.chat.presentation.model.InputState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = koinViewModel()
    val state: ChatState by viewModel.state.collectAsStateWithLifecycle()

    ChatScreenContent(
        state = state,
        processIntent = viewModel::processIntent,
    )
}

@Composable
private fun ChatScreenContent(
    state: ChatState,
    processIntent: (ChatIntent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = { ChatHeader(state.headerState) },
        bottomBar = { ChatInput(state.inputState, processIntent) }
    ) { innerPadding ->
        when (state.messagesState) {
            is ChatMessagesState.Ready -> ChatMessagesContent(
                modifier = Modifier.padding(innerPadding),
                items = state.messagesState.items
            )

            is ChatMessagesState.Loading -> ChatLoading(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun ChatHeader(state: HeaderState) {
    val context = LocalContext.current

    val avatarRes = remember(state.image) {
        context.resources.getIdentifier(state.image, "drawable", context.packageName)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = state.name,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun ChatMessagesContent(
    modifier: Modifier,
    items: List<ChatItem>
) {
    val listState = rememberLazyListState()

    LaunchedEffect(items.size) {
        listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentPadding = PaddingValues(8.dp),
        reverseLayout = true,
        state = listState,
    ) {
        items(items, key = { it.key }) { item ->
            when (item) {
                is ChatItem.Message -> MessageBubble(item)
                is ChatItem.DateTime -> DateTimeItem(item.value)
            }
        }
    }
}

@Composable
private fun ChatLoading(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
internal fun LoadingChatScreenPreview() {
    ChatScreenContent(
        state = ChatState(
            headerState = HeaderState(
                name = "Sarah",
                image = "avatar_sarah"
            ),
            messagesState = ChatMessagesState.Loading,
        )
    ) {}
}

@Preview
@Composable
internal fun ChatScreenPreview() {
    ChatScreenContent(
        state = ChatState(
            headerState = HeaderState(
                name = "Sarah",
                image = "avatar_sarah"
            ),
            inputState = InputState(isEnabled = true),
            messagesState = ChatMessagesState.Ready(
                items = listOf(
                    ChatItem.Message(
                        isOutgoing = false,
                        text = "Preview Message",
                        isGrouped = false,
                        key = 1,
                    ),
                    ChatItem.Message(
                        isOutgoing = true,
                        text = "Preview Message",
                        isGrouped = false,
                        key = 2,
                    ),
                    ChatItem.Message(
                        isOutgoing = true,
                        text = "Preview Message",
                        isGrouped = true,
                        key = 3,
                    ),
                    ChatItem.Message(
                        isOutgoing = true,
                        text = "Preview Message",
                        isGrouped = false,
                        key = 4,
                    ),
                    ChatItem.DateTime(
                        value = "Thursday 11:59",
                        key = 5,
                    ),
                )
            ),
        ),
    ) {}
}