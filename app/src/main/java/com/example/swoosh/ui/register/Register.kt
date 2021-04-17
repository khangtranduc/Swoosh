package com.example.swoosh.ui.register

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.swoosh.R
import com.example.swoosh.data.model.User
import com.example.swoosh.data.Repository
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.launch

class Register : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regi_text_background.visibility = View.VISIBLE

        lifecycleScope.launch {
            regi_text_background.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.translation_cycle))
        }

        register_btn.setOnClickListener{
            val name = register_name_et.text.toString()
            val email = register_email_et.text.toString()
            val password = register_password_et.text.toString()
            val confirmPassword = register_pass_confirm_et.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(requireContext(), "Some fields are empty", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
                    register_email_et.startAnimation(shakeAnim)
                    register_name_et.startAnimation(shakeAnim)
                    register_password_et.startAnimation(shakeAnim)
                    register_pass_confirm_et.startAnimation(shakeAnim)
                }
                return@setOnClickListener
            }

            if (password != confirmPassword){
                Toast.makeText(requireContext(), "Confirm password does not match", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
                    register_password_et.startAnimation(shakeAnim)
                    register_pass_confirm_et.startAnimation(shakeAnim)
                    register_pass_confirm_et.setText("")
                    register_password_et.setText("")
                }
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener{
                            if (it.isSuccessful){
                                Toast.makeText(requireContext(), "Email Verification Sent", Toast.LENGTH_SHORT).show()
                                Repository.updateUserParticulars(User(name), user.email.toString())
                                gotoLogIn()
                            } else{
                                Toast.makeText(requireContext(), "Email Verification failed to send", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        lifecycleScope.launch {
                            val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
                            register_email_et.startAnimation(shakeAnim)
                            register_name_et.startAnimation(shakeAnim)
                            register_password_et.startAnimation(shakeAnim)
                            register_pass_confirm_et.startAnimation(shakeAnim)
                        }
                        Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun navigateUp(){
        regi_text_background.visibility = View.GONE
        findNavController().navigateUp()
    }

    private fun gotoLogIn(){
        regi_text_background.visibility = View.GONE
        findNavController().navigate(RegisterDirections.gotoLogIn())
    }
}