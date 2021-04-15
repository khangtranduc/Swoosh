package com.example.swoosh.ui.on_boarding

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class OnBoardingTransform : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            when{
                position < -1 -> { //[-inft, -1)
                    alpha = 0f
                }
                position <= 0 -> { //[-1, 0]
                    alpha = position + 1
                    translationY = -50 * position
                }
                position <= 1 -> { //[0,1]
                    alpha = 1 - position
                    translationY = 50 * position
                }
            }
        }
    }
}