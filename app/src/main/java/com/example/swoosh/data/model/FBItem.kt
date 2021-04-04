package com.example.swoosh.data.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FBItem(
        var name: String = "",
        var type: String = "",
        var containables: HashMap<String, Containable> = hashMapOf(),
        var dateCreated: Long = System.currentTimeMillis()
) {


    @Serializable
    data class Containable(
            var name: String = "",
            var date: String = "",
            var details: String = ""
    ){
        companion object {
            fun parseToContainable(todo: Todolist.Todo): Containable {
                return Containable().apply {
                    name = todo.name
                    date = todo.due
                    details = todo.priority
                }
            }

            fun parseToContainable(note: NoteCollection.Note): Containable {
                return Containable().apply {
                    name = note.title
                    date = note.dateEdited
                    details = note.content
                }
            }

            fun parseToTodo(containable: Containable) : Todolist.Todo{
                return Todolist.Todo().apply {
                    name = containable.name
                    due = containable.date
                    priority = containable.details
                }
            }

            fun parseToNote(containable: Containable) : NoteCollection.Note{
                return NoteCollection.Note().apply {
                    title = containable.name
                    dateEdited = containable.date
                    content = containable.details
                }
            }
        }
    }

    companion object {
        fun parseToFBItem(boardItem: BoardItem) : FBItem{
            return FBItem().apply{
                name = boardItem.name
                dateCreated = boardItem.dateCreated

                if (boardItem is Todolist) {
                    type = "Todolist"
                    for ((key, value) in boardItem.todos) {
                        containables[key] = Containable.parseToContainable(value)
                    }
                } else if (boardItem is NoteCollection) {
                    type = "NoteCollection"
                    for ((key, value) in boardItem.notes) {
                        containables[key] = Containable.parseToContainable(value)
                    }
                }
            }
        }

        fun parseToBoardItem(fbitem: FBItem) : BoardItem{
            return if (fbitem.type == "Todolist"){
                Todolist().apply {
                    name = fbitem.name
                    for ((key, value) in fbitem.containables){
                        todos[key] = Containable.parseToTodo(value)
                    }
                    dateCreated = fbitem.dateCreated
                }
            }
            else{
                NoteCollection().apply {
                    name = fbitem.name
                    for ((key, value) in fbitem.containables){
                        notes[key] = Containable.parseToNote(value)
                    }
                }
            }
        }
    }
}