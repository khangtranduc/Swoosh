package com.example.swoosh.ui.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swoosh.R
import com.example.swoosh.data.model.NoteCollection
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.note_card.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class NoteDetail : Fragment() {

    private val args: NoteDetailArgs by navArgs()
    private val note: NoteCollection.Note by lazy {
        Json.decodeFromString(args.note)
    }
    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if (viewModel.note.value == null){
            viewModel.updateNote(note)
        }

        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.note.observe(viewLifecycleOwner){
            updateNoteUI(it)
        }

        updateNoteUI(viewModel.note.value!!)

        note_back_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        note_edit_btn.setOnClickListener {
            findNavController().navigate(NoteDetailDirections.gotoEdit(args.noteCollection, args.boardID, args.note, true))
        }
    }

    private fun updateNoteUI(note: NoteCollection.Note){
        note_details_title_tv.text = note.title
        note_details_date_tv.text = note.getDateStr()
        note_details_content_tv.text = note.content
    }

}