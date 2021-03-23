package com.example.swoosh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.swoosh.ui.nav.BottomSheet
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), 
        NavController.OnDestinationChangedListener{

    private val bottomSheet: BottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        supportFragmentManager.findFragmentById(R.id.bottom_sheet) as BottomSheet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener(this)
        bottomSheet.close()

        expand_btn.setOnClickListener{
            Log.d("debug", "expand_btn clicked")
            bottomSheet.toggle()
        }

        add_media.setOnClickListener{

        }

        bottomSheet.behaviour.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED){
                    add_board_fab.show()
                    bottom_up_arrow.animate().rotation(0f)
                            .setDuration(300)
                            .setInterpolator(DecelerateInterpolator())
                    destination_title.animate().alpha(1f)
                            .setDuration(300)
                            .setInterpolator(DecelerateInterpolator())
                }
                else{
                    add_board_fab.hide()
                    bottom_up_arrow.animate().rotation(180f)
                            .setDuration(300)
                            .setInterpolator(DecelerateInterpolator())
                    destination_title.animate().alpha(0f)
                            .setDuration(300)
                            .setInterpolator(DecelerateInterpolator())
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id){
            R.id.nav_home -> {
                showBottomBar()
                bottomSheet.close()
                destination_title.text = "Boards"
                fabAdd()
                Log.d("debug", "Home")
            }
            R.id.nav_chat_window -> {
                fabSend()
                showChatET()
                Log.d("debug", "Chat Window")
            }
            R.id.nav_chat -> {
                bottomSheet.close()
                destination_title.text = "Chats"
                fabAddPerson()
                hideChatET()
                Log.d("debug", "Chat")
            }
            R.id.logIn -> {
                hideBottomBar()
                Log.d("debug", "LogIn")
            }
            R.id.register -> {
                hideBottomBar()
                Log.d("debug", "Register")
            }
        }
    }

    private fun fabSend(){
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        add_board_fab.setImageResource(R.drawable.ic_baseline_send_24)
    }

    private fun showChatET(){
        expand_btn.visibility = View.GONE
        chat_et_container.visibility = View.VISIBLE
        chat_et_container.alpha = 0f
        chat_et_container.animate().alpha(1F)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
    }

    private fun hideChatET() {
        chat_et_container.visibility = View.GONE
        expand_btn.visibility = View.VISIBLE
        expand_btn.alpha = 0f
        expand_btn.animate().alpha(1f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
    }

    private fun fabAddPerson(){
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        add_board_fab.setImageResource(R.drawable.ic_baseline_person_add_alt_1_24)
    }

    private fun fabAdd(){
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        add_board_fab.setImageResource(R.drawable.ic_baseline_add_24)
    }

    private fun showBottomBar(){
        bottom_app_bar.animate().translationY(0F)
        add_board_fab.show()
    }

    private fun hideBottomBar(){
        bottom_app_bar.translationY = 200F
        add_board_fab.hide()
    }
}