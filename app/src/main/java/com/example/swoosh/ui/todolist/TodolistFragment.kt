package com.example.swoosh.ui.todolist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.example.swoosh.R
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.board_view.BoardView
import com.example.swoosh.ui.dialog_fragments.BoardItemOverflowDialog
import com.example.swoosh.ui.dialog_fragments.TodoCreationDialog
import com.example.swoosh.utils.currentNavigationFragment
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.board_item_overflow.*
import kotlinx.android.synthetic.main.fab_add_sheet.*
import kotlinx.android.synthetic.main.fragment_todolist.*
import java.text.SimpleDateFormat
import java.util.*

class TodolistFragment(private val todolist: Todolist, private val boardID: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todolist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todolist_name_tv.text = todolist.name
        todolist_date_created_tv.text = "Date Created: ${todolist.getDateStr()}"

        val queryRef = Firebase.database.reference.child("itemStore")
                .child(boardID).child(todolist.dateCreated.toString()).child("containables")

        val options = FirebaseRecyclerOptions.Builder<Todolist.Todo>()
                .setLifecycleOwner(viewLifecycleOwner)
                .setQuery(queryRef){
                    Todolist.Todo().apply {
                        name = it.child("name").value as String
                        due = it.child("date").value as String
                        priority = it.child("details").value as String
                    }
                }
                .build()

        todo_recycler.apply {
            adapter = FirebaseAdapter(options, todolist, boardID, requireActivity())
            layoutManager = LinearLayoutManager(requireContext())
        }

        add_todo_btn.setOnClickListener{
            TodoCreationDialog(todolist, boardID).show(childFragmentManager, TodoCreationDialog.TAG)
        }

        todolist_overflow_btn.setOnClickListener{
            BoardItemOverflowDialog(todolist, boardID).show(childFragmentManager, BoardItemOverflowDialog.TAG)
        }
    }

    override fun toString(): String {
        return todolist.name
    }
}