package com.seweryn.chat.di

import com.seweryn.chat.data.di.dataModule
import com.seweryn.chat.presentation.di.presentationModule
import org.koin.dsl.module

val chatModule = module {
    includes(dataModule, presentationModule)
}