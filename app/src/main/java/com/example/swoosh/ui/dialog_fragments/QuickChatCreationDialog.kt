package com.example.swoosh.ui.dialog_fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.Convo
import kotlinx.android.synthetic.main.quick_chat_creation.*

class QuickChatCreationDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setWindowAnimations(R.style.dialog_animation)
        }

        return inflater.inflate(R.layout.quick_chat_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_quick_chat_btn.setOnClickListener{
            val name = quick_chat_name_et.text.toString()
            val membersCSV = quick_chat_members_et.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(membersCSV)){
                Toast.makeText(requireContext(), "Empty Fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (name.contains(":")){
                Toast.makeText(requireContext(), "':' is not an allowed character", Toast.LENGTH_SHORT).show()
            }

            Repository.pushQuickChatToFirebase(Convo(
                    "", "$name:${resources.getString(R.string.anonymous_board)}"
            ), membersCSV, requireContext())

            dismiss()
        }
    }

    companion object{
        val TAG = "quick_chat_creation_dialog"
    }
}