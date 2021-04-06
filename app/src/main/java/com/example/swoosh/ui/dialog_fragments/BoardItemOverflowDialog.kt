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
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.board_item_creation_dialog.*
import kotlinx.android.synthetic.main.board_item_overflow.*
import kotlinx.android.synthetic.main.confirm_delete.*

class BoardItemOverflowDialog(
        private val boardItem: BoardItem,
        private val boardID: String
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setWindowAnimations(R.style.dialog_animation)
        }

        return inflater.inflate(R.layout.board_item_overflow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (boardItem is Todolist){
            board_item_name_et.hint = "Todolist Name"
        }
        else if (boardItem is NoteCollection){
            board_item_name_et.hint = "Note Collection Name"
        }

        board_item_creation_card.visibility = View.INVISIBLE
        create_board_item_btn.text = "Edit"
        board_item_name_et.setText(boardItem.name)


        create_board_item_btn.setOnClickListener {
            val newName = board_item_name_et.text.toString()

            if (TextUtils.isEmpty(newName)){
                Toast.makeText(requireContext(), "Name Field is Empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            boardItem.name = newName
            Repository.updateFBItem(FBItem.parseToFBItem(boardItem), boardID)
            showOptions(board_item_creation_card)
            dismiss()
        }

        edit_board_item_btn.setOnClickListener {
            hideOptions(board_item_creation_card)
        }

        delete_board_item_btn.setOnClickListener {
            hideOptions(confirm_delete_card)

        }

        cancel_delete_btn.setOnClickListener{
            showOptions(confirm_delete_card)
        }

        confirm_delete_btn.setOnClickListener{
            Repository.deleteFBItem(FBItem.parseToFBItem(boardItem), boardID)
            dismiss()
        }
    }

    private fun hideOptions(view: View) {
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        TransitionManager.beginDelayedTransition(board_item_edit_container, sharedAxis)
        options_board_item_card.visibility = View.INVISIBLE
        view.visibility = View.VISIBLE
    }

    private fun showOptions(view: View) {
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        TransitionManager.beginDelayedTransition(board_item_edit_container, sharedAxis)
        options_board_item_card.visibility = View.VISIBLE
        view.visibility = View.INVISIBLE
    }

    companion object{
        val TAG = "board_item_overflow_dialog"
    }
}