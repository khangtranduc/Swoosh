package com.example.swoosh.ui.dialog_fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
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

    private var uriGlobal: Uri? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setWindowAnimations(R.style.dialog_animation)
        }

        return inflater.inflate(R.layout.fragment_user_edit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Glide.with(requireContext())
            .load(Repository.user.value?.uri)
            .placeholder(R.drawable.avatar_1)
            .into(profile_preview)

        edit_image_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, Repository.IMAGE_REQUEST)
        }

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

            if (uriGlobal != null){
                user.uri = uriGlobal
            }
            else{
                repo_user?.let { user.uri = it.uri }
            }

            uriGlobal?.let { Repository.putUserImageInStorage(it, requireContext()) }
            Firebase.auth.currentUser?.let { Repository.updateUserParticulars(user, it.email.toString()) }

            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Repository.IMAGE_REQUEST){
            if (resultCode == Activity.RESULT_OK && data != null){
                val uri: Uri? = data.data
                uriGlobal = uri

                Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.avatar_1)
                        .into(profile_preview)
            }
        }
    }

    companion object{
        const val TAG = "user_edit_dialog"
    }
}