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
import com.example.swoosh.data.model.Convo
import kotlinx.android.synthetic.main.board_item_creation_dialog.*

class ConvoEditDialog(private val convo: Convo) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setWindowAnimations(R.style.dialog_animation)
        }

        return inflater.inflate(R.layout.board_item_creation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        board_item_name_et.hint = "Chat Name"
        create_board_item_btn.text = "Edit"

        board_item_name_et.setText(convo.name.substringBefore(":"))

        create_board_item_btn.setOnClickListener{
            val newName = board_item_name_et.text

            if (TextUtils.isEmpty(newName)){
                Toast.makeText(requireContext(), "Name field empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            convo.name = "$newName:${convo.name.substringAfter(":")}"

            Repository.updateConvo(convo)

            dismiss()
        }
    }

    companion object{
        val TAG = "convo_edit_dialog"
    }
}