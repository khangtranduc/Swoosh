package com.example.swoosh.utils

import androidx.fragment.app.Fragment
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.notes.NoteFragment
import com.example.swoosh.ui.todolist.TodolistFragment
import java.util.*
import kotlin.collections.ArrayList

object BoardUtils {
    fun getBoardItemFragments(boardItems: SortedMap<String, BoardItem>, boardID: String): ArrayList<Fragment>{
        val returnArray = arrayListOf<Fragment>()

        for ((key, value) in boardItems){
            if (value is Todolist){
                returnArray.add(TodolistFragment(value, boardID))
            }
            else if (value is NoteCollection){
                returnArray.add(NoteFragment(value, boardID))
            }
        }

        return returnArray
    }
}