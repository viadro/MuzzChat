package com.seweryn.chat.data

import com.seweryn.chat.domain.UserRepository
import com.seweryn.chat.domain.model.User

internal class UserRepositoryImpl : UserRepository {

    override fun getCurrentUser() = User(
        id = CURRENT_USER_ID,
        image = "avatar_sarah",
        name = "Piotr"
    )

    override fun getOtherUser() = User(
        id = OTHER_USER_ID,
        image = "avatar_sarah",
        name = "Sarah"
    )
}
