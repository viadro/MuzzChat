package com.seweryn.chat.domain

import com.seweryn.chat.domain.model.User

internal interface UserRepository {

    fun getCurrentUser(): User

    fun getOtherUser(): User
}
