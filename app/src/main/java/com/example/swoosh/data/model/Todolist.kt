package com.example.swoosh.data.model

import com.example.swoosh.R
import kotlinx.android.synthetic.main.todo_creation_dialog.view.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Serializable
@SerialName("Todolist")
data class Todolist(
        override var name: String = "",
        var todos: HashMap<String, Todo> = hashMapOf(),
        override var dateCreated: Long = 0L
) : BoardItem() {
    @Serializable
    data class Todo(
        var name: String = "",
        var due: String = "",
        var priority: String = "p4",
    )

    fun getDateStr(): String{
        return getDateStr(dateCreated, "dd/MM/yyyy")
    }

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

        fun pStringToID(pString: String): Int{
            return when(pString){
                "p1" -> R.id.p1_btn
                "p2" -> R.id.p2_btn
                "p3" -> R.id.p3_btn
                else -> R.id.p4_btn
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

    override fun clone() : BoardItem {
        return Todolist(name, HashMap(todos), dateCreated)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Todolist){
            this.name == other.name &&
                    this.todos == other.todos &&
                    this.dateCreated == other.dateCreated
        }
        else{
            super.equals(other)
        }
    }
}