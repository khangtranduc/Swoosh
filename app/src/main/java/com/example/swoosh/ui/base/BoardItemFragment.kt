package com.example.swoosh.ui.base

import androidx.fragment.app.Fragment
import com.example.swoosh.data.model.BoardItem

abstract class BoardItemFragment() : Fragment() {
    abstract var id: Long

    abstract fun theSame(item: BoardItemFragment) : Boolean

    abstract fun setValue(fragment: BoardItemFragment)

    abstract fun clone(): BoardItemFragment
}