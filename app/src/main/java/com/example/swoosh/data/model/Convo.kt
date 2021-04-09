package com.example.swoosh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Convo")
data class Convo(
        var name: String = "",
        var lastMessage: String = "",
        var timeStamp: Long = 0
) {
}