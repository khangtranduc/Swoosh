package com.example.swoosh.ui.on_boarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.swoosh.NavigationGraphDirections
import com.example.swoosh.R
import com.example.swoosh.data.model.OBPage
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoarding : Fragment() {

    private val items = arrayListOf(
        OBPage("Welcome", "to your new collaborative hub"),
        OBPage("Create and edit boards", "Each board houses collections of notes and todolists which are backed up to Firebase", R.drawable.ic_dashboard_ob),
        OBPage("Share them with the world", "You can share your boards with other Swoosh users via their email handles", R.drawable.ic_share_ob),
        OBPage("Communicate with your team", "Each board, upon creation, generates its own chat group", R.drawable.ic_groups_ob),
        OBPage("Quick Chat", "You can also directly create a chat group with other Swoosh users via their email handles", R.drawable.ic_chat_ob)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        on_boarding_viewpager.adapter = OnBoardingAdapter(requireActivity()).apply { submitList(items) }
        TabLayoutMediator(on_boarding_dot_indicator, on_boarding_viewpager){_,_ -> }.attach()

        next_obpage_btn.setOnClickListener{
            if (on_boarding_viewpager.currentItem < items.size - 1){
                on_boarding_viewpager.currentItem++
            }
            else{
                val prefs = requireContext().getSharedPreferences("onboardingSP", Context.MODE_PRIVATE)
                with(prefs.edit()){
                    putBoolean("hasOnboarding", true)
                    apply()
                }
                findNavController().navigate(NavigationGraphDirections.actionGlobalLogin())
            }
        }

        previous_obpage_btn.setOnClickListener {
            if (on_boarding_viewpager.currentItem > 0) {
                on_boarding_viewpager.currentItem--
            }
        }
    }

}