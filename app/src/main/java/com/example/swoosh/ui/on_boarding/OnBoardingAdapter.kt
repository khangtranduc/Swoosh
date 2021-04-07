package com.example.swoosh.ui.on_boarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.swoosh.data.model.OBPage

class OnBoardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val pages = arrayListOf<OBPage>()

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment = OBPageFragment(pages[position])

    fun submitList(newPages: List<OBPage>){
        pages.apply {
            clear()
            addAll(newPages)
            notifyDataSetChanged()
        }
    }
}