package com.seweryn.chat.data

import com.seweryn.chat.domain.UserRepository
import com.seweryn.chat.domain.model.User

internal class UserRepositoryImpl : UserRepository {

    override fun getOtherUser() = User(
        image = "avatar_sarah",
        name = "Sarah"
    )
}
