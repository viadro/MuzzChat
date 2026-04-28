package com.seweryn.chat.domain

import com.seweryn.chat.domain.model.User

internal interface UserRepository {

    fun getOtherUser(): User
}
