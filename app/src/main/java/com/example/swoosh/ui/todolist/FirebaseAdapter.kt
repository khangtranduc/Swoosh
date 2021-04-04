package com.example.swoosh.ui.todolist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.dialog_fragments.TodoDeletionDialog
import com.example.swoosh.utils.PolySeri
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.serialization.encodeToString

class FirebaseAdapter(options: FirebaseRecyclerOptions<Todolist.Todo>,
                      private val todolist: Todolist,
                      private val boardID: String,
                      private val activity: FragmentActivity)
    : FirebaseRecyclerAdapter<Todolist.Todo, FirebaseAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.todo_name_tv)
        val due = itemView.findViewById<TextView>(R.id.due_date_tv)
        val checkBox = itemView.findViewById<CheckBox>(R.id.check_btn)

        fun bind(todo: Todolist.Todo){
            itemView.setOnLongClickListener{
                TodoDeletionDialog(todo, todolist, boardID).show(activity.supportFragmentManager, TodoDeletionDialog.TAG)
                false
            }

            checkBox.setOnClickListener{

                Repository.deleteFBItem(
                        FBItem.parseToFBItem(todolist),
                        FBItem.Containable.parseToContainable(todo),
                        boardID
                )
                Toast.makeText(activity.baseContext, "Todo Completed!", Toast.LENGTH_SHORT).show()
            }

            name.text = todo.name
            due.text = todo.due
            checkBox.buttonTintList = activity.getColorStateList(Todolist.parsePriority(todo.priority))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Todolist.Todo) {
        holder.bind(model)
    }
}