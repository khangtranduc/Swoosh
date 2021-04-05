package com.example.swoosh.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swoosh.R
import com.example.swoosh.data.model.NoteCollection
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.note_card.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NoteDetail : Fragment() {

    private val args: NoteDetailArgs by navArgs()
    private val note: NoteCollection.Note by lazy {
        Json.decodeFromString(args.note)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note_title_tv.text = note.title
        note_date_tv.text = note.dateEdited
        note_content_tv.text = note.content

        note_back_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        note_edit_btn.setOnClickListener {
            findNavController().navigate(NoteDetailDirections.gotoEdit(args.noteCollection, args.boardID, args.note))
        }
    }

}