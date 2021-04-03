package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Board(
        var name: String = "",
        var members: ArrayList<Member> = arrayListOf(),
        var items: HashMap<String, FBItem> = hashMapOf(),
        var id: String = ""
) {
    @Serializable
    data class Member(
            var email: String = "",
            var name: String = ""
    ){
        fun clone() = Member(email, name)
    }

    fun getActualItems() : HashMap<String, BoardItem>{
        val actualItems = hashMapOf<String, BoardItem>()

        for ((key, value) in items){
            actualItems[key] = FBItem.parseToBoardItem(value)
        }

        return actualItems
    }
}