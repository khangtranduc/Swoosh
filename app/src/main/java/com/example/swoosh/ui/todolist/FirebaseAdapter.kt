package com.example.swoosh.ui.todolist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.utils.PolySeri
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.serialization.encodeToString

class FirebaseAdapter(options: FirebaseRecyclerOptions<Todolist.Todo>,
                      private val activity: FragmentActivity)
    : FirebaseRecyclerAdapter<Todolist.Todo, FirebaseAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.todo_name_tv)
        val due = itemView.findViewById<TextView>(R.id.due_date_tv)
        val checkBox = itemView.findViewById<CheckBox>(R.id.check_btn)

        fun bind(todo: Todolist.Todo){
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