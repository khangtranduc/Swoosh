package com.example.swoosh.data.model

import com.example.swoosh.utils.Priority
import kotlinx.serialization.Serializable

@Serializable
data class Todolist(
    var name: String,
    var todos: ArrayList<Todo>
) : BoardItem{
    @Serializable
    data class Todo(
        var name: String,
        var due: String,
        var priority: Priority,
        var subtasks: ArrayList<SubTask>
    )

    //TODO: this can be added later
    @Serializable
    data class SubTask(
        var name: String
    )
}