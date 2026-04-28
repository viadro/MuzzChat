package com.seweryn.muzzchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seweryn.chat.presentation.ui.ChatScreen
import com.seweryn.muzzchat.ui.theme.MuzzChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuzzChatTheme {
                ChatScreen()
            }
        }
    }
}