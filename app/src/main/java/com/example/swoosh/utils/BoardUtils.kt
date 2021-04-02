package com.example.swoosh.utils

import androidx.fragment.app.Fragment
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.notes.NoteFragment
import com.example.swoosh.ui.todolist.TodolistFragment

object BoardUtils {
    fun getBoardItemFragments(boardItems: HashMap<String, BoardItem>): ArrayList<Fragment>{
        val returnArray = arrayListOf<Fragment>()

        for ((key, value) in boardItems){
            if (value is Todolist){
                returnArray.add(TodolistFragment(value))
            }
            else if (value is NoteCollection){
                returnArray.add(NoteFragment(value))
            }
        }

        return returnArray
    }
}