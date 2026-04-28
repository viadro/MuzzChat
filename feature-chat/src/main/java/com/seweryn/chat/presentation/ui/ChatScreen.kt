package com.seweryn.chat.presentation.ui


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seweryn.chat.presentation.model.ChatItem
import com.seweryn.chat.presentation.model.ChatState

@Composable
fun ChatScreen() {

}

@Composable
private fun ChatScreenContent(state: ChatState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(state.items) { item ->
                when(item) {
                    is ChatItem.Message -> MessageBubble(item)
                    is ChatItem.DateTime -> DateTimeItem(item.value)
                }
            }
        }
    }
}

@Preview
@Composable
internal fun ChatScreenPreview() {
    ChatScreenContent(
        state = ChatState(
            items = listOf(
                ChatItem.DateTime(
                    value = "Thursday 11:59",
                ),
                ChatItem.Message(
                    isOutgoing = true,
                    text = "Preview Message",
                    isGrouped = false,
                ),
                ChatItem.Message(
                    isOutgoing = true,
                    text = "Preview Message",
                    isGrouped = true,
                ),
                ChatItem.Message(
                    isOutgoing = true,
                    text = "Preview Message",
                    isGrouped = false,
                ),
                ChatItem.Message(
                    isOutgoing = false,
                    text = "Preview Message",
                    isGrouped = false,
                )
            )
        ),
    )
}