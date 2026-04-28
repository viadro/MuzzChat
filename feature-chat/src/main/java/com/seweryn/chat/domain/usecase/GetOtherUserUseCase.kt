package com.seweryn.chat.domain.usecase

import com.seweryn.chat.domain.UserRepository

internal class GetOtherUserUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke() = userRepository.getOtherUser()
}
