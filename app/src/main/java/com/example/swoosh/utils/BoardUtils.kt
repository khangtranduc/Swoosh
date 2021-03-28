package com.example.swoosh.utils

import androidx.fragment.app.Fragment
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.notes.NoteFragment
import com.example.swoosh.ui.todolist.TodolistFragment

object BoardUtils {
    fun getBoardItemFragments(boardItems: ArrayList<BoardItem>): ArrayList<Fragment>{
        val returnArray = arrayListOf<Fragment>()

        for (i in boardItems){
            if (i is Todolist){
                returnArray.add(TodolistFragment(i))
            }
            else if (i is NoteCollection){
                returnArray.add(NoteFragment(i))
            }
        }

        return returnArray
    }
}