package com.example.swoosh.ui.notes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swoosh.R
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.utils.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
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

        if (viewModel.note.value == null || viewModel.note.value?.isEmpty() == true){
            viewModel.updateNote(note)
        }

        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set default anims
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)

        viewModel.note.observe(viewLifecycleOwner){
            updateNoteUI(it)
        }

        updateNoteUI(viewModel.note.value!!)

        note_back_btn.setOnClickListener {
            findNavController().navigateUp()
            viewModel.updateNote(NoteCollection.Note())
        }

        note_edit_btn.setOnClickListener {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            findNavController().navigate(NoteDetailDirections.gotoEdit(args.noteCollection, args.boardID, args.note, true))
        }
    }

    private fun updateNoteUI(note: NoteCollection.Note){
        note_details_title_tv.text = note.title
        note_details_date_tv.text = note.getDateStr()
        note_details_content_tv.text = note.content
    }

}