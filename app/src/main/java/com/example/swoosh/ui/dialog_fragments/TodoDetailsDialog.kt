package com.example.swoosh.ui.dialog_fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.Todolist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.todo_card.*
import kotlinx.android.synthetic.main.todo_creation_dialog.*
import org.w3c.dom.Text

class TodoDetailsDialog(
        private val todo: Todolist.Todo,
        private val todolist: Todolist,
        private val boardID: String
) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_creation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todo_name_et.setText(todo.name)
        due_date_et.setText(todo.due)
        create_todo_btn.text = "Edit"

        create_todo_btn.setOnClickListener{
            val todoNew = Todolist.Todo().apply {
                name = if (TextUtils.isEmpty(todo_name_et.text)){
                    todo.name
                } else{
                    todo_name_et.text.toString()
                }

                due = if (TextUtils.isEmpty(due_date_et.text)){
                    todo.due
                } else{
                    due_date_et.text.toString()
                }

                priority = Todolist.parsePriority(radio_group_todo_creation.checkedRadioButtonId)
            }

            Repository.updateFBItem(
                    FBItem.parseToFBItem(todolist),
                    FBItem.Containable.parseToContainable(todo),
                    FBItem.Containable.parseToContainable(todoNew),
                    boardID
            )

            Toast.makeText(requireContext(), "Todo Updated!", Toast.LENGTH_SHORT).show()

            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    companion object{
        val TAG = "todo_details_dialog"
    }
}