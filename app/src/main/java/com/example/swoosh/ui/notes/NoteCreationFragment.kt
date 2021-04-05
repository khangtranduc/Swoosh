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
import kotlinx.android.synthetic.main.fragment_note_creation.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NoteCreationFragment : Fragment() {

    private val args: NoteCreationFragmentArgs by navArgs()
    private val note: NoteCollection.Note by lazy{
        Json.decodeFromString(args.note)
    }
    private val noteCollection: NoteCollection by lazy {
        Json.decodeFromString(args.noteCollection)
    }
    private val boardID: String by lazy{
        args.boardID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note_title_et.setText(note.title)
        note_content_et.setText(note.content)

        note_creation_back_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        note_creation_save_btn.setOnClickListener {
            //TODO save note code
            findNavController().navigateUp()
        }
    }

}