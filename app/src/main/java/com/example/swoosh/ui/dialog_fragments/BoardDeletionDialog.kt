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
import kotlinx.android.synthetic.main.board_item_creation_dialog.*
import kotlinx.android.synthetic.main.message_deletion_dialog.*

class BoardDeletionDialog(private val board: Board) : DialogFragment() {
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

        deletion_prompt_tv.text = "Delete Board?"

        cancel_delete_message_btn.setOnClickListener {
            dismiss()
        }

        message_delete_btn.setOnClickListener {
            Repository.deleteBoard(board)
            dismiss()
            Toast.makeText(requireContext(), "Board Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        val TAG = "board_deletion_dialog"
    }
}