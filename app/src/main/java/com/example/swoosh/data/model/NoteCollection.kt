package com.example.swoosh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
@SerialName("NoteCollection")
data class NoteCollection(
        override var name: String = "",
        var notes: HashMap<String, Note> = hashMapOf(),
        override var dateCreated: Long = 0L
) : BoardItem() {
    @Serializable
    data class Note(
        var title: String = "",
        var content: String = "",
        var dateEdited: String = "0"
    ){
        fun getDateStr(): String{
            return SimpleDateFormat("MMM, dd, yyyy").format(Date(dateEdited.toLong()))
        }
    }
}