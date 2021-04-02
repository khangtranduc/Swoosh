package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
        var name: String = "",
        var age: Long = -1,
        var from: String = "",
        var handle: String = ""
) {
}