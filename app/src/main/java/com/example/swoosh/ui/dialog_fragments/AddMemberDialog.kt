package com.example.swoosh.ui.dialog_fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
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
import kotlinx.android.synthetic.main.board_creation_dialog.*
import kotlinx.android.synthetic.main.board_item_creation_dialog.*

class AddMemberDialog (
        private val boardID: String,
        private val boardViewContext: Context
        ): DialogFragment() {
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

        return inflater.inflate(R.layout.board_item_creation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        board_item_name_et.hint = "Member's email"
        board_item_name_et.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        create_board_item_btn.text = "Add Member"

        create_board_item_btn.setOnClickListener{
            val email = board_item_name_et.text.toString().substringBefore(",")

            if (TextUtils.isEmpty(email)){
                Toast.makeText(requireContext(), "Empty Field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val member = Board.Member(email)

            Repository.addMemberToBoard(boardID, member, boardViewContext)

            dismiss()
        }

    }

    companion object{
        val TAG = "add_member_dialog"
    }
}