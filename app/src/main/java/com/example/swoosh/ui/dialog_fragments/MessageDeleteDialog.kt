package com.example.swoosh.ui.dialog_fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Message
import kotlinx.android.synthetic.main.message_deletion_dialog.*

class MessageDeleteDialog(private val messageID: String, private val convoID: String, private val sender: String) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setWindowAnimations(R.style.dialog_animation)
        }

        return inflater.inflate(R.layout.message_deletion_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel_delete_message_btn.setOnClickListener{
                dismiss()
        }

        message_delete_btn.setOnClickListener{
            Repository.deleteMessageFromFirebase(messageID, convoID, sender)
            dismiss()
        }
    }

    companion object{
        val TAG = "message_delete_dialog"
    }
}