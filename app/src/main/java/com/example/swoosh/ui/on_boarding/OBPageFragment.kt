package com.example.swoosh.ui.on_boarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.example.swoosh.R
import com.example.swoosh.data.model.OBPage
import kotlinx.android.synthetic.main.fragment_o_b_page.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OBPageFragment(private val obpage: OBPage) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_o_b_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val animEnter = AnimationUtils.loadAnimation(requireContext(), R.anim.onboard_appear)

        obpage_text_tv.text = obpage.title

        obpage_content_tv.text = obpage.content

        obpage_img.setImageResource(obpage.img)
    }

}