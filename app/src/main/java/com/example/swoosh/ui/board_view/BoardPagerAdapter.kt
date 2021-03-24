package com.example.swoosh.ui.board_view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BoardPagerAdapter (fm: FragmentManager, numberOfTabs: Int): FragmentPagerAdapter(fm, numberOfTabs) {
    var tabCount: Int = numberOfTabs

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return Fragment()
    }
}