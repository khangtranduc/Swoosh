package com.example.swoosh.ui.dialog_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.NoteCollection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.note_action_dialog.*

class NoteActionDialog(
        private val note: NoteCollection.Note,
        private val noteCollection: NoteCollection,
        private val boardID: String
) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_action_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delete_note_btn.setOnClickListener {

            Repository.deleteFromFBItem(
                    FBItem.parseToFBItem(noteCollection),
                    FBItem.Containable.parseToContainable(note),
                    boardID
            )

            Toast.makeText(requireContext(), "Note Deleted!", Toast.LENGTH_SHORT).show()

            dismiss()
        }
    }

    companion object{
        val TAG = "note_action_dialog"
    }
}