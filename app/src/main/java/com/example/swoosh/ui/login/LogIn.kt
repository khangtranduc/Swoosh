package com.example.swoosh.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.swoosh.R
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogIn : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn.setOnClickListener{
            findNavController().navigate(LogInDirections.gotoHome())
        }

        register_now.setOnClickListener{
            findNavController().navigate(LogInDirections.gotoRegister())
        }
    }
}