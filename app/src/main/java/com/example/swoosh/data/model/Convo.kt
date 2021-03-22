package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Convo(
        var name: String,
        var lastMessage: String,
        var timeStamp: Long
) {
}