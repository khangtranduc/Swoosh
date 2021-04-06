package com.example.swoosh.ui.dialog_fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.core.Repo
import kotlinx.android.synthetic.main.board_action_dialog.*

class BoardActionDialog(private val board: Board) : BottomSheetDialogFragment() {
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
            BoardEditDialog(board).show(requireActivity().supportFragmentManager, BoardEditDialog.TAG)
            dismiss()
        }

        delete_board_btn.setOnClickListener {
            Repository.deleteBoard(board)
            dismiss()
            Toast.makeText(requireContext(), "Board Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        val TAG = "board_action_dialog"
    }
}