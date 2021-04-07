package com.example.swoosh.ui.board_view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.ui.base.BoardItemFragment
import com.example.swoosh.utils.BoardUtils
import com.example.swoosh.utils.PagerDiffUtil
import java.util.*

class BoardPagerAdapter(private val activity: FragmentActivity): FragmentStateAdapter(activity) {

    val fragments = arrayListOf<BoardItemFragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemId(position: Int): Long {
        return fragments[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.any{ it.id == itemId}
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()){
            val tag = "f" + holder.itemId
            val fragment = activity.supportFragmentManager.findFragmentByTag(tag)
            if (fragment != null){
                (fragment as BoardItemFragment).setValue(fragments[position])
            }
            else{
                super.onBindViewHolder(holder, position, payloads)
            }
        }
        else{
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun submitList(newFragments: List<BoardItemFragment>){
        Log.d("debug", "$fragments $newFragments")
        val callback = PagerDiffUtil(fragments, newFragments)
        val diff = DiffUtil.calculateDiff(callback)
        fragments.apply {
            clear()
            addAll(newFragments)
        }
        diff.dispatchUpdatesTo(this)
    }
}