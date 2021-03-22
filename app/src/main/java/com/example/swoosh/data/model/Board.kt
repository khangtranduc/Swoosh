package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Board(
        var name: String,
        var members: ArrayList<Member>
) {
    @Serializable
    data class Member(
            var name: String,
            var email: String
    )
}