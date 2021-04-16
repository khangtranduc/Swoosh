package com.example.swoosh

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.example.swoosh.data.model.User
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Message
import com.example.swoosh.ui.dialog_fragments.BoardCreationDialog
import com.example.swoosh.ui.dialog_fragments.UserEditDialog
import com.example.swoosh.ui.board_view.BoardView
import com.example.swoosh.ui.chat.Chat
import com.example.swoosh.ui.chat.ChatWindow
import com.example.swoosh.ui.chat.ChatWindowArgs
import com.example.swoosh.ui.dialog_fragments.QuickChatCreationDialog
import com.example.swoosh.ui.home.Home
import com.example.swoosh.ui.home.HomeViewModel
import com.example.swoosh.ui.nav.BottomSheet
import com.example.swoosh.ui.register.Register
import com.example.swoosh.utils.SandwichState
import com.example.swoosh.utils.Status
import com.example.swoosh.utils.currentNavigationFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fab_add_sheet.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.user_info.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), 
        NavController.OnDestinationChangedListener {

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

        //connect buttons with functions
        setOnClicks()

        //set up view model observers for bottom bar
        Repository.user.observe(this){
            setUpBottomBarUser(it)
        }

        //set up onboarding
        setUpOnboarding()

        //set up current data on start up
        Firebase.auth.currentUser?.let { Repository.fetchUser(it.email.toString(), baseContext) }

        //set up bottom sheet
        bottomSheet.behaviour.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomDrawer: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED){
                    if (id != R.id.nav_settings && id != R.id.logIn){
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

    private fun setUpOnboarding(){
        val prefs = getSharedPreferences("onboardingSP", Context.MODE_PRIVATE)
        val hasOnboarding = prefs.getBoolean("hasOnboarding", false)
        if (!hasOnboarding){
            findNavController(R.id.nav_host_fragment).navigate(NavigationGraphDirections.actionGlobalOnBoarding())
        }
        else{
            findNavController(R.id.nav_host_fragment).navigate(NavigationGraphDirections.actionGlobalLogin())
        }
    }

    private fun setOnClicks(){
        expand_btn.setOnClickListener{
            bottomSheet.toggle()
            if (Repository.user.value == null){
                reload_user_btn.visibility = View.VISIBLE
                age_tv.visibility = View.INVISIBLE
                user_edit_btn.visibility = View.GONE
                from_tv.visibility = View.INVISIBLE
            }
        }

        add_media.setOnClickListener{

        }

        add_board_fab.setOnClickListener{
            dynamicFabToggle()
        }

        dynamic_button.setOnClickListener{
            dynamicOnCLick(dynamic_button.tag as Int)
        }

        logout_btn.setOnClickListener{
            Firebase.auth.signOut()
            findNavController(R.id.nav_host_fragment).navigate(NavigationGraphDirections.actionGlobalLogin())
        }

        user_edit_btn.setOnClickListener{
            UserEditDialog().show(supportFragmentManager, UserEditDialog.TAG)
        }

        reload_user_btn.setOnClickListener{
            Firebase.auth.currentUser?.let { Repository.fetchUser(it.email.toString(), baseContext) }
        }
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
                else if (id == R.id.nav_board_view){
                    (supportFragmentManager.currentNavigationFragment as BoardView).navigateUp()
                }
                else if (id == R.id.register){
                    (supportFragmentManager.currentNavigationFragment as Register).navigateUp()
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
                hideChatET()
                bottomSheet.close()
                destination_title.text = "Boards"
                dynamic_button.visibility = View.VISIBLE
                fabAdd()
            }
            R.id.nav_chat_window -> {
                fabSend()
                showChatET()
                showBottomBar()
                dynamic_button.visibility = View.GONE
            }
            R.id.nav_chat -> {
                bottomSheet.close()
                showBottomBar()
                destination_title.text = "Chats"
                fabAddPerson()
                hideChatET()
                dynamic_button.visibility = View.VISIBLE
            }
            R.id.logIn -> {
                hideBottomBar()
                bottomSheet.close()
            }
            R.id.register -> {
                hideBottomBar()
            }
            R.id.nav_search -> {
                performHideBottomBar()
            }
            R.id.nav_settings -> {
                performHideBottomBar()
                bottomSheet.close()
            }
            R.id.nav_board_view -> {
                performHideBottomBar()
                dynamic_button.visibility = View.GONE
            }
            R.id.nav_note_creation -> {
                add_board_fab.hide()
            }
            R.id.nav_note_detail -> {
                add_board_fab.hide()
            }
            R.id.nav_on_boarding -> {
                hideBottomBar()
            }
        }
    }

    fun emptyChatET(){
        chat_et.setText("")
    }

    fun rotateOpen(){
        bottom_up_arrow.animate().rotation(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
        destination_title.animate().alpha(1f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
    }

    private fun setUpBottomBarUser(user: User){

        age_tv.visibility = View.VISIBLE
        from_tv.visibility = View.VISIBLE
        reload_user_btn.visibility = View.GONE
        user_edit_btn.visibility = View.VISIBLE

        name_tv.text = user.name

        if (user.age == -1L){
            age_tv.text = "Age: N/A"
        }
        else{
            age_tv.text = "Age: ${user.age}"
        }

        if (user.from == ""){
            from_tv.text = "From: N/A"
        }
        else{
            from_tv.text = "From: ${user.from}"
        }

        Glide.with(this)
            .load(user.uri)
            .placeholder(R.drawable.ic_launcher_background)
            .into(profile_image)

        Glide.with(this)
            .load(user.uri)
            .placeholder(R.drawable.ic_launcher_background)
            .into(circle_avatar)
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
        when(drawable){
            R.drawable.ic_baseline_settings_24 -> {settingsAction()}
            R.drawable.ic_baseline_search_24 -> {searchAction()}
        }
    }

    private fun dynamicFabToggle(){
        when(id){
            R.id.nav_home -> {
                BoardCreationDialog().show(supportFragmentManager, BoardCreationDialog.TAG)
            }
            R.id.nav_chat_window -> {
                val convoID = (supportFragmentManager.currentNavigationFragment as ChatWindow).getConvoId()
                sendMessage(convoID)
            }
            R.id.nav_chat -> {
                QuickChatCreationDialog().show(supportFragmentManager, QuickChatCreationDialog.TAG)
            }
        }
    }

    private fun sendMessage(convoID: String){
        val mes = chat_et.text.toString()

        if (!TextUtils.isEmpty(mes)){
            Repository.user.value?.let {
                val message = Message(
                        Firebase.auth.currentUser?.email.toString(),
                        it.name, mes, System.currentTimeMillis())
                Repository.pushMessageToFirebase(message, convoID)
                chat_et.setText("")
            }
        }
    }

    private fun dynamicReplaceIcon(drawable: Int){
        dynamic_button.setImageResource(drawable)
        dynamic_button.tag = drawable
    }

    private fun searchAction(){
        supportFragmentManager.currentNavigationFragment?.let {
            it.exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
            it.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ false)
        }
        val action = NavigationGraphDirections.actionGlobalSearch()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    private fun settingsAction(){
        supportFragmentManager.currentNavigationFragment?.let {
            it.exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
            it.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
        }
        val settings = NavigationGraphDirections.actionGlobalSettings()
        findNavController(R.id.nav_host_fragment).navigate(settings)
    }

    private fun fabSend(){
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        lifecycleScope.launch {
            delay(300)
            add_board_fab.setImageResource(R.drawable.ic_baseline_send_24)
        }
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
        lifecycleScope.launch {
            delay(300)
            add_board_fab.setImageResource(R.drawable.ic_baseline_person_add_alt_1_24)
        }
    }

    private fun fabAdd(){
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        add_board_fab.setImageResource(R.drawable.ic_baseline_add_24)
    }

    fun hideBottomBarOnly(){
        bottom_app_bar.animate().translationY(200f).setDuration(250).setInterpolator(AccelerateInterpolator())
    }

    fun showBottomBarOnly(){
        bottom_app_bar.animate().translationY(0f).setDuration(250).setInterpolator(DecelerateInterpolator())
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