package com.seweryn.chat.data

internal class MessageGenerator {

    fun generate() = REPLIES.random()

    private companion object {
        private val REPLIES = listOf(
            "Wowsa sounds fun",
            "Yeh for sure that works. What time do you think?",
            "Ok cool!",
            "Actually just about to go shopping, got any recommendations for a good shoe store? I'm a fashion disaster",
            "The last one went on for hours"
        )
    }
}
