package com.example.swoosh.ui.dialog_fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.swoosh.R
import com.example.swoosh.data.model.User
import com.example.swoosh.data.Repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_edit_dialog.*


class UserEditDialog() : DialogFragment() {

    interface UserEditDialogCallback {
        fun onDismissListener()
    }

    private val callback: UserEditDialogCallback by lazy{
        (requireActivity() as UserEditDialogCallback)
    }

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

            val user = User()
            val repo_user = Repository.user.value

            if (!TextUtils.isEmpty(name)){
                user.name = name
            }
            else{
                repo_user?.let { user.name = it.name }
            }

            if (!TextUtils.isEmpty(age)){
                user.age = age.toLong()
            }
            else{
                repo_user?.let { user.age = it.age }
            }

            if (!TextUtils.isEmpty(from)){
                user.from = from
            }
            else{
                repo_user?.let { user.from = it.from }
            }

            Firebase.auth.currentUser?.let { Repository.updateUserParticulars(user, it.email.toString()) }

            dismiss()
        }
    }

    companion object{
        const val TAG = "user_edit_dialog"
    }
}