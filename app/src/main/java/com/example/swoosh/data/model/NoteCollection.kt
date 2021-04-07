package com.example.swoosh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
            return getDateStr(dateEdited.toLong(), "MMM dd, yyyy")
        }

        fun isEmpty() : Boolean{
            return title == "" && content == "" && dateEdited == "0"
        }
    }

    fun getDateStr(): String{
        return getDateStr(dateCreated, "dd/MM/yyyy")
    }

    override fun clone(): BoardItem {
        return NoteCollection(name, HashMap(notes), dateCreated)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is NoteCollection){
            (this.name == other.name &&
                    this.notes == other.notes &&
                    this.dateCreated == other.dateCreated)
        }
        else{
            super.equals(other)
        }
    }
}