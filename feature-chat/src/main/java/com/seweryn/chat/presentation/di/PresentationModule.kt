package com.seweryn.chat.presentation.di

import com.seweryn.chat.presentation.ChatViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::ChatViewModel)
}