package com.example.swoosh.data.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Polymorphic
@Serializable
abstract class BoardItem() {
    abstract var name: String
    abstract var dateCreated: Long

    companion object{
        fun getDateStr(time: Long, format: String): String{
            return SimpleDateFormat(format).format(Date(time))
        }
    }
}