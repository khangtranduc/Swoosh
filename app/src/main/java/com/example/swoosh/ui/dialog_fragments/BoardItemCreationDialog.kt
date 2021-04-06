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
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import kotlinx.android.synthetic.main.board_item_creation_dialog.*

class BoardItemCreationDialog(
        private val isTodolist: Boolean,
        private val board: Board
) : DialogFragment() {

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

        if (isTodolist){
            board_item_name_et.hint = "Todolist Name"
        }
        else{
            board_item_name_et.hint = "Note Collection Name"
        }

        create_board_item_btn.setOnClickListener{
            val item_name = board_item_name_et.text.toString()

            if (TextUtils.isEmpty(item_name)){
                Toast.makeText(requireContext(), "Name field empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isTodolist){
                Repository.pushBoardItemToBoard(board, FBItem(item_name, "Todolist"))
            }
            else{
                Repository.pushBoardItemToBoard(board, FBItem(item_name, "NoteCollection"))
            }

            dismiss()
        }
    }

    companion object{
        val TAG = "board_item_creation_dialog"
    }
}