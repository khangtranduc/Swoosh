package com.example.swoosh.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                login_btn.isEnabled = true
                return@setOnClickListener
            }

            login_btn.isEnabled = false
            login_tv.text = "Loading..."
            login_progress.visibility = View.VISIBLE
            login_btn.alpha = 0.5f

            Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            if (Firebase.auth.currentUser?.isEmailVerified != true){
                                Snackbar.make(view, "Email is not verified!", Snackbar.LENGTH_SHORT).show()
                                login_btn.isEnabled = true
                                login_progress.visibility = View.INVISIBLE
                                login_tv.text = "Log In"
                                login_btn.alpha = 1f
                                Firebase.auth.signOut()
                            }
                            else{
                                Toast.makeText(requireContext(), "Sign in Success!", Toast.LENGTH_SHORT).show()
                                Firebase.auth.currentUser?.let { user -> Repository.fetchUser(user.email.toString(), requireActivity().baseContext) }
                                logIn()
                            }
                        }
                        else{
                            login_btn.isEnabled = true
                            Toast.makeText(requireContext(), "Sign in failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
        }

        register_now.setOnClickListener{
            findNavController().navigate(LogInDirections.gotoRegister())
        }
    }

    private fun logIn(){
        homeViewModel.fetchBoards()
        chatViewModel.fetchConvos()
        findNavController().navigate(LogInDirections.gotoHome())
    }
}