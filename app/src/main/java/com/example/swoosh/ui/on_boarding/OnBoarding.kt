package com.example.swoosh.ui.on_boarding

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.example.swoosh.NavigationGraphDirections
import com.example.swoosh.R
import com.example.swoosh.data.model.OBPage
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoarding : Fragment() {

    private var staggeredPosition: Int = 0

    private val items = arrayListOf(
        OBPage("Welcome", "to your new collaborative hub"),
        OBPage("Create and edit boards", "Each board houses collections of notes and todolists which are backed up to Firebase", R.drawable.ic_dashboard_ob),
        OBPage("Share them with the world", "You can share your boards with other Swoosh users via their email handles", R.drawable.ic_share_ob),
        OBPage("Communicate with your team", "Each board, upon creation, generates its own chat group", R.drawable.ic_groups_ob),
        OBPage("Quick Chat", "You can also directly create a chat group with other Swoosh users via their email handles", R.drawable.ic_chat_ob),
        OBPage("You're all set", "Click the below button to proceed to the app")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        on_boarding_viewpager.apply {
            adapter = OnBoardingAdapter(requireActivity()).apply { submitList(items) }
            setPageTransformer(OnBoardingTransform())
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    when {
                        position == items.size - 1 -> {
                            continueToggle()
                        }
                        (position == items.size - 2 &&
                                staggeredPosition == items.size - 1) -> {
                                    continueToggle()
                                }
                        position == 0 -> {
                            previous_obpage_btn.visibility = View.INVISIBLE
                        }
                        else -> {
                            previous_obpage_btn.visibility = View.VISIBLE
                        }
                    }
                    staggeredPosition = position
                }
            })
        }

        TabLayoutMediator(on_boarding_dot_indicator, on_boarding_viewpager){_,_ -> }.attach()

        next_obpage_btn.setOnClickListener{
            if (on_boarding_viewpager.currentItem < items.size - 1){
                on_boarding_viewpager.currentItem++
            }
        }

        continue_ob_btn.setOnClickListener{
            val prefs = requireContext().getSharedPreferences("onboardingSP", Context.MODE_PRIVATE)
            with(prefs.edit()){
                putBoolean("hasOnboarding", true)
                apply()
            }
            findNavController().navigate(NavigationGraphDirections.actionGlobalLogin())
        }

        previous_obpage_btn.setOnClickListener {
            if (on_boarding_viewpager.currentItem > 0) {
                on_boarding_viewpager.currentItem--
            }
        }
    }

    private fun continueToggle(){
        val views = listOf<View>(next_obpage_btn, continue_ob_btn).sortedBy{ !it.isVisible }
        val transition = MaterialContainerTransform().apply{
            startView = views.first()
            endView = views.last()
            duration = 300
            addTarget(views.last())
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(ob_container, transition)
        views.first().visibility = View.INVISIBLE
        views.last().visibility = View.VISIBLE
    }

}