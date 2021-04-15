package com.example.swoosh.ui.on_boarding

import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.swoosh.R
import com.example.swoosh.data.model.OBPage

class OnBoardingAdapter(private val activity: FragmentActivity) : FragmentStateAdapter(activity) {

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