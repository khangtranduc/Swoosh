package com.example.swoosh

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.swoosh.ui.nav.BottomSheet
import com.example.swoosh.ui.nav.HalfCounterClockwiseRotateSlideAction
import com.example.swoosh.utils.SandwichState
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class MainActivity : AppCompatActivity(), 
        NavController.OnDestinationChangedListener{

    private val bottomSheet: BottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        supportFragmentManager.findFragmentById(R.id.bottom_sheet) as BottomSheet
    }
    private val appBarConfiguration: AppBarConfiguration by lazy{
        AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_chat)
        )
    }
    private var id: Int = R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener(this)
        bottomSheet.close()

        //setup dynamic button
        dynamicReplaceIcon(R.drawable.ic_baseline_search_24)

        expand_btn.setOnClickListener{
            Log.d("debug", "expand_btn clicked")
            bottomSheet.toggle()
        }

        add_media.setOnClickListener{

        }

        dynamic_button.setOnClickListener{
            dynamicOnCLick(dynamic_button.tag as Int)
        }

        bottomSheet.behaviour.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomDrawer: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED){
                    if (id != R.id.nav_settings){
                        add_board_fab.show()
                    }
                    dynamicReplaceIcon(R.drawable.ic_baseline_search_24)
                    bottomSheet.animateCloseSandwich()
                    rotateOpen()
                }
                else{
                    add_board_fab.hide()
                    dynamicReplaceIcon(R.drawable.ic_baseline_settings_24)
                    if (bottomSheet.sandwichState == SandwichState.CLOSED) rotateClose()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onBackPressed() {
        when{
            bottomSheet.sandwichState == SandwichState.OPEN -> bottomSheet.animateCloseSandwich()
            (bottomSheet.behaviour.state != BottomSheetBehavior.STATE_HIDDEN
                    && bottomSheet.behaviour.state != BottomSheetBehavior.STATE_COLLAPSED)
                    -> bottomSheet.close()
            (bottomSheet.behaviour.state == BottomSheetBehavior.STATE_HIDDEN
                    || bottomSheet.behaviour.state == BottomSheetBehavior.STATE_COLLAPSED)
                    -> {
                if (id == R.id.nav_home || id == R.id.logIn){
                    finishAffinity()
                }
                else{
                    findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                }
            }
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        id = destination.id
        when (destination.id){
            R.id.nav_home -> {
                showBottomBar()
                bottomSheet.close()
                destination_title.text = "Boards"
                dynamic_button.visibility = View.VISIBLE
                fabAdd()
                Log.d("debug", "Home")
            }
            R.id.nav_chat_window -> {
                fabSend()
                showChatET()
                dynamic_button.visibility = View.GONE
                Log.d("debug", "Chat Window")
            }
            R.id.nav_chat -> {
                bottomSheet.close()
                showBottomBar()
                destination_title.text = "Chats"
                fabAddPerson()
                hideChatET()
                dynamic_button.visibility = View.VISIBLE
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
            R.id.nav_search -> {
                performHideBottomBar()
                Log.d("debug", "Search")
            }
            R.id.nav_settings -> {
                performHideBottomBar()
                bottomSheet.close()
                Log.d("debug", "Settings")
            }
        }
    }

    fun rotateOpen(){
        bottom_up_arrow.animate().rotation(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
        destination_title.animate().alpha(1f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
    }

    fun rotateClose(){
        bottom_up_arrow.animate().rotation(180f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
        destination_title.animate().alpha(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
    }

    private fun dynamicOnCLick(drawable: Int){
        Log.d("debug", "${R.drawable.ic_baseline_search_24} ${R.drawable.ic_baseline_settings_24} $drawable")
        when(drawable){
            R.drawable.ic_baseline_settings_24 -> {settingsAction()}
            R.drawable.ic_baseline_search_24 -> {searchAction()}
        }
    }

    private fun dynamicReplaceIcon(drawable: Int){
        dynamic_button.setImageResource(drawable)
        dynamic_button.tag = drawable
    }

    private fun searchAction(){
        val search = NavigationGraphDirections.actionGlobalSearch()
        findNavController(R.id.nav_host_fragment).navigate(search)
    }

    private fun settingsAction(){
        val settings = NavigationGraphDirections.actionGlobalSettings()
        findNavController(R.id.nav_host_fragment).navigate(settings)
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

    private fun performHideBottomBar(){
        bottom_app_bar.animate().translationY(200f)
        add_board_fab.hide()
    }

    private fun hideBottomBar(){
        bottom_app_bar.translationY = 200F
        add_board_fab.hide()
    }
}