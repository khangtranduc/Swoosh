package com.example.swoosh.ui.board_view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.swoosh.ui.todolist.TodolistFragment

class BoardPagerAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>): FragmentPagerAdapter(fm, fragments.size) {
    var tabCount: Int = fragments.size

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}