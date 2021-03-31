package com.example.swoosh.ui.register

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.swoosh.R
import com.example.swoosh.data.model.User
import com.example.swoosh.data.repository.Repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*

class Register : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_btn.setOnClickListener{
            val name = register_name_et.text.toString()
            val email = register_email_et.text.toString()
            val password = register_password_et.text.toString()
            val confirmPassword = register_pass_confirm_et.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(requireContext(), "Some fields are empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword){
                Toast.makeText(requireContext(), "Confirm password does not match", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun gotoLogIn(){
        findNavController().navigate(RegisterDirections.gotoLogIn())
    }
}