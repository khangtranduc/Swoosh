package com.example.swoosh.ui.board_view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.ui.base.BoardItemFragment
import com.example.swoosh.utils.BoardUtils
import java.util.*

class BoardPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    private val fragments = arrayListOf<BoardItemFragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemId(position: Int): Long {
        return fragments[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.any{ it.id == itemId}
    }

    fun submitList(items: List<BoardItemFragment>){
        fragments.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }
}