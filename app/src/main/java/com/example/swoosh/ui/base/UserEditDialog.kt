package com.example.swoosh.ui.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.swoosh.MainActivity
import com.example.swoosh.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_edit_dialog.*


class UserEditDialog() : DialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return inflater.inflate(R.layout.fragment_user_edit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        edit_btn.setOnClickListener{
            val name = name_edit_et.text.toString()
            val age = age_edit_et.text.toString()
            val from = country_edit_et.text.toString()

            val user = Firebase.auth.currentUser
            user?.let{
                val userRef = Firebase.database.reference.child("users").child(it.email.toString().substringBefore("@"))

                Log.d("debug", it.email.toString().substringBefore("@"))

                if (!TextUtils.isEmpty(name)){
                    userRef.child("name").setValue(name)
                }

                if (!TextUtils.isEmpty(age)){
                    userRef.child("age").setValue(age)
                }

                if (!TextUtils.isEmpty(from)){
                    userRef.child("from").setValue(from)
                }

                (requireActivity() as MainActivity).setUpBottomBarUser(it)
            }

            dismiss()
        }
    }

    companion object{
        const val TAG = "user_edit_dialog"
    }
}