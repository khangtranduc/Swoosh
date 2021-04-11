package com.example.swoosh.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
@SerialName("Convo")
data class Convo(
        var id: String = "",
        var name: String = "",
        var lastMessage: String = "",
        var timeStamp: Long = 0
){}

@Serializable
data class Message(
        var senderEmail: String = "",
        var sender: String = "",
        var message: String = "",
        var timeStamp: Long = 0,
        var id: String = ""
){
    @SuppressLint("SimpleDateFormat")
    fun getTimeStampString(): String{
        return SimpleDateFormat("HH:mm").format(Date(timeStamp))
    }
}