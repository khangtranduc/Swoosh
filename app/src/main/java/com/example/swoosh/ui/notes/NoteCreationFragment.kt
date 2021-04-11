package com.example.swoosh.ui.notes

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.NoteCollection
import com.google.android.material.transition.MaterialSharedAxis
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
    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel.updateNote(note)

        return inflater.inflate(R.layout.fragment_note_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateNoteUI(note)

        note_creation_back_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        note_creation_save_btn.setOnClickListener {
            //TODO save note code
            val title = note_title_et.text.toString()
            val content = note_content_et.text.toString()

            if (TextUtils.isEmpty(title)){
                Toast.makeText(requireContext(), "Title can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val noteNew = NoteCollection.Note(title, content, "${System.currentTimeMillis()}")

            if (args.asEdit){
                Repository.updateToFBItem(FBItem.parseToFBItem(noteCollection),
                        FBItem.Containable.parseToContainable(note),
                        FBItem.Containable.parseToContainable(noteNew),
                        boardID)
                viewModel.updateNote(noteNew)
            }
            else{
                Repository.pushToFBItem(FBItem.parseToFBItem(noteCollection), FBItem.Containable.parseToContainable(noteNew), boardID)
                viewModel.updateNote(NoteCollection.Note())
            }

            findNavController().navigateUp()
        }
    }

    private fun updateNoteUI(note: NoteCollection.Note){
        note_title_et.setText(note.title)
        note_content_et.setText(note.content)
    }

}