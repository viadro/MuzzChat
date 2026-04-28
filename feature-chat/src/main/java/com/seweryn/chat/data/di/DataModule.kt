package com.seweryn.chat.data.di

import com.seweryn.chat.data.UserRepositoryImpl
import com.seweryn.chat.domain.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}