package com.example.swoosh.ui.base

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.MainActivity

class ScrollListener(private val activity: FragmentActivity) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            // Scrolling up
            (activity as MainActivity).hideBottomBarOnly()
        } else {
            // Scrolling down
            (activity as MainActivity).showBottomBarOnly()
        }
    }
}