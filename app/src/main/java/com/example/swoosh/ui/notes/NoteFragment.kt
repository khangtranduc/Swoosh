package com.example.swoosh.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.NavigationGraphDirections
import com.example.swoosh.R
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.base.BoardItemFragment
import com.example.swoosh.ui.board_view.BoardView
import com.example.swoosh.ui.board_view.BoardViewDirections
import com.example.swoosh.ui.dialog_fragments.BoardItemOverflowDialog
import com.example.swoosh.ui.notes.FirebaseAdapter
import com.example.swoosh.utils.currentNavigationFragment
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_todolist.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class NoteFragment(private val noteCollection: NoteCollection, private val boardID: String) : BoardItemFragment() {

    override var id: Long = noteCollection.dateCreated

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteCol_name_tv.text = noteCollection.name
        noteCol_date_created_tv.text = "Date Created: ${noteCollection.getDateStr()}"

        val queryRef = Firebase.database.reference.child("itemStore")
                .child(boardID).child(noteCollection.dateCreated.toString()).child("containables")

        val options = FirebaseRecyclerOptions.Builder<NoteCollection.Note>()
                .setLifecycleOwner(viewLifecycleOwner)
                .setQuery(queryRef){
                    NoteCollection.Note().apply {
                        title = it.child("name").value as String
                        dateEdited = it.child("date").value as String
                        content = it.child("details").value as String
                    }
                }
                .build()

        note_recycler.apply {
            adapter = FirebaseAdapter(options, noteCollection, boardID, requireActivity())
            layoutManager = LinearLayoutManager(requireContext())
        }

        add_note_btn.setOnClickListener{
            findNavController().navigate(NavigationGraphDirections.actionGlobalNoteCreation(
                    Json.encodeToString(noteCollection),
                    boardID,
                    Json.encodeToString(NoteCollection.Note())
            ))
        }

        noteCol_overflow_btn.setOnClickListener {
            BoardItemOverflowDialog(noteCollection, boardID).show(childFragmentManager, BoardItemOverflowDialog.TAG)
        }
    }

    override fun theSame(item: BoardItemFragment): Boolean {
        return if (item is NoteFragment){
            this.noteCollection == item.noteCollection
        }
        else{
            false
        }
    }

    override fun toString(): String {
        return noteCollection.name
    }
}