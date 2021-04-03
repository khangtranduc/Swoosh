package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Board(
        var name: String = "",
        var members: ArrayList<Member> = arrayListOf(),
        var items: HashMap<String, BoardItem> = hashMapOf(),
        var id: String = ""
) {
    @Serializable
    data class Member(
            var email: String = "",
            var name: String = ""
    ){
        fun clone() = Member(email, name)
    }
}