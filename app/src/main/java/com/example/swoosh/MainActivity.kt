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
                Log.d("debug", "Home")
            }
            R.id.nav_chat -> {
                bottomSheet.close()
                destination_title.text = "Chats"
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

    private fun showBottomBar(){
        bottom_app_bar.animate().translationY(0F)
        add_board_fab.show()
    }

    private fun hideBottomBar(){
        bottom_app_bar.translationY = 200F
        add_board_fab.hide()
    }
}