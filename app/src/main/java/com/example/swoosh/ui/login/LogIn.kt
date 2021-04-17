package com.example.swoosh.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.ui.chat.ChatViewModel
import com.example.swoosh.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogIn : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        homeViewModel.clearBoards()
        chatViewModel.clearChat()

        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = Firebase.auth.currentUser
        text_background.visibility = View.VISIBLE

        lifecycleScope.launch {
            text_background.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.translation_cycle))
        }

        if (currentUser != null && currentUser.isEmailVerified){
            logIn()
        }
        else if (currentUser != null && !currentUser.isEmailVerified){
            Snackbar.make(view, "Go verify your email address", Snackbar.LENGTH_SHORT).show()
            Firebase.auth.signOut()
        }

        login_btn.setOnClickListener{
            val email = email_et.text.toString()
            val password = password_et.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(requireContext(), "Some fields are empty!", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    email_et.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation))
                    password_et.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation))
                }
                login_btn.isEnabled = true
                return@setOnClickListener
            }

            toggleLogin()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            if (Firebase.auth.currentUser?.isEmailVerified != true){
                                Snackbar.make(view, "Email is not verified!", Snackbar.LENGTH_SHORT).show()
                                toggleLogin()
                                Firebase.auth.signOut()
                            }
                            else{
                                Toast.makeText(requireContext(), "Sign in Success!", Toast.LENGTH_SHORT).show()
                                Firebase.auth.currentUser?.let { user -> Repository.fetchUser(user.email.toString(), requireActivity().baseContext) }
                                logIn()
                            }
                        }
                        else{
                            toggleLogin()
                            lifecycleScope.launch {
                                email_et.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation))
                                password_et.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation))
                                email_et.setText("")
                                password_et.setText("")
                            }
                            Toast.makeText(requireContext(), "Sign in failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
        }

        register_now.setOnClickListener{
            text_background.visibility = View.GONE
            findNavController().navigate(LogInDirections.gotoRegister())
        }
    }

    fun toggleLogin(){
        if (login_btn.isEnabled){
            login_progress.visibility = View.VISIBLE
            login_tv.text = "Loading..."
            login_btn.alpha = 0.5f
        }
        else{
            login_progress.visibility = View.INVISIBLE
            login_tv.text = "Log In"
            login_btn.alpha  = 1f
        }
        login_btn.isEnabled = !login_btn.isEnabled
    }

    private fun logIn(){
        homeViewModel.fetchBoards()
        chatViewModel.fetchConvos()
        text_background.visibility = View.GONE
        findNavController().navigate(LogInDirections.gotoHome())
    }
}