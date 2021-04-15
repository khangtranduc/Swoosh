package com.example.swoosh.ui.on_boarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
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

        obpage_text_tv.text = obpage.title

        obpage_content_tv.text = obpage.content

        if (obpage.title == "You're all set"){
            ob_img_container.visibility = View.GONE
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(12, resources.getDimension(R.dimen.on_boarding_top_text_displaced).toInt(), 12, 8)
            obpage_text_tv.layoutParams = layoutParams
        }
        else{
            obpage_img.setImageResource(obpage.img)
        }
    }

}