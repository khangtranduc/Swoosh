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
import com.example.swoosh.data.model.Convo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.board_action_dialog.*

class QuickChatActionDialog(private val convo: Convo) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return inflater.inflate(R.layout.board_action_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_board_btn.setOnClickListener {
            ConvoEditDialog(convo).show(requireActivity().supportFragmentManager, ConvoEditDialog.TAG)
            dismiss()
        }

        delete_board_btn.setOnClickListener {
            ConvoDeletionDialog(convo.id).show(requireActivity().supportFragmentManager, ConvoDeletionDialog.TAG)
            dismiss()
        }
    }

    companion object{
        val TAG = "quick_chat_action_dialog"
    }
}