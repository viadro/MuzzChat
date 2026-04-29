package com.seweryn.chat.data.di

import androidx.room.Room
import com.seweryn.chat.data.MessagesRepositoryImpl
import com.seweryn.chat.data.UserRepositoryImpl
import com.seweryn.chat.data.storage.ChatDatabase
import com.seweryn.chat.data.storage.ChatDatabase.Companion.DATABASE_NAME
import com.seweryn.chat.domain.MessagesRepository
import com.seweryn.chat.domain.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::MessagesRepositoryImpl) bind MessagesRepository::class
    single {
        Room.databaseBuilder(
            get(),
            ChatDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single { get<ChatDatabase>().messageDao() }
}