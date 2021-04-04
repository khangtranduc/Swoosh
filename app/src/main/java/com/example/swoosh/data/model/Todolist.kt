package com.example.swoosh.data.model

import com.example.swoosh.R
import kotlinx.android.synthetic.main.todo_creation_dialog.view.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("Todolist")
data class Todolist(
        override var name: String = "",
        var todos: HashMap<String, Todo> = hashMapOf(),
        var dateCreated: Long = 0L
) : BoardItem() {
    @Serializable
    data class Todo(
        var name: String = "",
        var due: String = "",
        var priority: String = "p4",
    )

    //var subtasks: ArrayList<SubTask> = arrayListOf()
    //TODO: this can be added later
    @Serializable
    data class SubTask(
        var name: String = ""
    )

    companion object {
        fun parsePriority(radioID: Int) : String{
            return when (radioID){
                R.id.p1_btn -> "p1"
                R.id.p2_btn -> "p2"
                R.id.p3_btn -> "p3"
                R.id.p4_btn -> "p4"
                else -> ""
            }
        }

        fun parsePriority(pString: String) : Int{
            return when (pString){
                "p1" -> android.R.color.holo_red_dark
                "p2" -> android.R.color.holo_orange_dark
                "p3" -> android.R.color.holo_blue_light
                "p4" -> android.R.color.darker_gray
                else -> android.R.color.holo_green_dark
            }
        }
    }
}