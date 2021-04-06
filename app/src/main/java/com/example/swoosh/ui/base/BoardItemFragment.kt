package com.example.swoosh.ui.base

import androidx.fragment.app.Fragment

open class BoardItemFragment : Fragment() {
    open var id: Long = 0

    open fun theSame(item: BoardItemFragment) : Boolean{
        return false
    }
}