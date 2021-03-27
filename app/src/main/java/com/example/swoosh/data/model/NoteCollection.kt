package com.example.swoosh.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteCollection(
    var title: String,
    var notes: ArrayList<Note>
) : BoardItem{
    @Serializable
    data class Note(
        var title: String,
        var content: String,
        var dateEdited: String
    )
}