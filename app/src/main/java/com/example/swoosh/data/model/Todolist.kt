package com.example.swoosh.data.model

import com.example.swoosh.utils.Priority
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Todolist")
data class Todolist(
        override var name: String = "",
        var todos: ArrayList<Todo> = arrayListOf()
) : BoardItem() {
    @Serializable
    data class Todo(
        var name: String = "",
        var due: String = "",
        var priority: String = "p4",
        var subtasks: ArrayList<SubTask> = arrayListOf()
    )

    //TODO: this can be added later
    @Serializable
    data class SubTask(
        var name: String = ""
    )
}