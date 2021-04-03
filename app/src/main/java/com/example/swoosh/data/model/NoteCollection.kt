package com.example.swoosh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NoteCollection")
data class NoteCollection(
        override var name: String = "",
        var notes: HashMap<String, Note> = hashMapOf()
) : BoardItem() {
    @Serializable
    data class Note(
        var title: String = "",
        var content: String = "",
        var dateEdited: String = ""
    )
}