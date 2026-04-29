package com.seweryn.chat.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seweryn.chat.presentation.model.ChatItem

private val MESSAGE_BUBBLE_CORNER_RADIUS = 12.dp
private val MESSAGE_BUBBLE_PADDING = 64.dp

@Composable
internal fun MessageBubble(message: ChatItem.Message) {
    Box(
        modifier = Modifier
            .padding(
                top = if (message.isGrouped) 2.dp else 8.dp,
                start = if (message.isOutgoing) MESSAGE_BUBBLE_PADDING else 0.dp,
                end = if (message.isOutgoing) 0.dp else MESSAGE_BUBBLE_PADDING,
            )
            .fillMaxWidth(),
        contentAlignment = if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        MessageBubbleText(
            modifier = Modifier.background(
                color = if (message.isOutgoing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    topStart = MESSAGE_BUBBLE_CORNER_RADIUS,
                    topEnd = MESSAGE_BUBBLE_CORNER_RADIUS,
                    bottomStart = if (message.isOutgoing) MESSAGE_BUBBLE_CORNER_RADIUS else 0.dp,
                    bottomEnd = if (message.isOutgoing) 0.dp else MESSAGE_BUBBLE_CORNER_RADIUS,
                )
            ),
            text = message.text,
            textColor = if (message.isOutgoing) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MessageBubbleText(
    modifier: Modifier,
    text: String,
    textColor: Color,
) {
    Text(
        modifier = modifier.padding(8.dp),
        text = text,
        color = textColor,
        fontSize = 14.sp,
    )
}

@Preview
@Composable
internal fun OutgoingMessageBubblePreview() {
    MessageBubble(
        message = ChatItem.Message(
            isOutgoing = true,
            text = "Preview Message",
            key = 1,
        ),
    )
}

@Preview
@Composable
internal fun IncomingMessageBubblePreview() {
    MessageBubble(
        message = ChatItem.Message(
            isOutgoing = false,
            text = "Preview Message",
            key = 1,
        ),
    )
}

@Preview
@Composable
internal fun GroupedMessageBubblesPreview() {
    Column {
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = false,
                key = 1,
            ),
        )
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = true,
                key = 2,
            ),
        )
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = true,
                key = 3,
            ),
        )
    }
}

@Preview
@Composable
internal fun NotGroupedMessageBubblesPreview() {
    Column {
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = false,
                key = 1,
            ),
        )
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = false,
                key = 2,
            ),
        )
        MessageBubble(
            message = ChatItem.Message(
                isOutgoing = true,
                text = "Preview Message",
                isGrouped = false,
                key = 3,
            ),
        )
    }
}