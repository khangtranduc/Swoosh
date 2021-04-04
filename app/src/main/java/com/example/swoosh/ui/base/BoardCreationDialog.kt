package com.example.swoosh.ui.base

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
import com.example.swoosh.data.model.Board
import kotlinx.android.synthetic.main.board_creation_dialog.*

class BoardCreationDialog : DialogFragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return inflater.inflate(R.layout.board_creation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_board_btn.setOnClickListener{
            val boardName = board_name_et.text.toString()
            val membersCSV = board_members_et.text.toString()

            Repository.pushBoardToFirebase(Board(boardName), membersCSV, requireContext())

            dismiss()
        }

    }

    companion object{
        val TAG = "board_creation_dialog"
    }
}