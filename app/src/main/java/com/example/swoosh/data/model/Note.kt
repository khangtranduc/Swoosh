package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    var title: String,
    var content: String
) : BoardItem{
}