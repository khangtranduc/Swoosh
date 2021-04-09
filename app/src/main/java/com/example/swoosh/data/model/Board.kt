package com.example.swoosh.data.model

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Serializable
@SerialName("Board")
data class Board(
        var name: String = "",
        var members: ArrayList<Member> = arrayListOf(),
        var id: String = ""
) {
    @Serializable
    data class Member(
            var email: String = "",
            var name: String = ""
    ){
        fun clone() = Member(email, name)
    }

    companion object{
        fun getActualItems(map: SortedMap<String, FBItem>) : SortedMap<String, BoardItem> {
            val actualItems = sortedMapOf<String, BoardItem>()
            for ((key, value) in map){
                actualItems[key] = FBItem.parseToBoardItem(value)
            }

            return actualItems
        }
    }
}