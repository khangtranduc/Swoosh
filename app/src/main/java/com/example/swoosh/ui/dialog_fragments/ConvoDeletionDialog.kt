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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.core.Repo
import kotlinx.android.synthetic.main.message_deletion_dialog.*

class ConvoDeletionDialog(private val convoID: String) : DialogFragment() {
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

        deletion_prompt_tv.text = "Delete Chat?"

        message_delete_btn.setOnClickListener {
            Repository.deleteConvo(convoID)
            dismiss()
        }

        cancel_delete_message_btn.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        val TAG = "convo_deletion_dialog"
    }
}