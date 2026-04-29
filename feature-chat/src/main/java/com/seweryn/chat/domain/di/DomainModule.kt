package com.seweryn.chat.domain.di

import com.seweryn.chat.domain.usecase.SendMessageUseCase
import com.seweryn.chat.domain.util.TimeProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val domainModule = module {
    factoryOf(::SendMessageUseCase)
    factoryOf(::TimeProvider)
}
