package com.example.swoosh.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.NavigationGraphDirections
import com.example.swoosh.R
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.ui.dialog_fragments.NoteActionDialog
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FirebaseAdapter(options: FirebaseRecyclerOptions<NoteCollection.Note>,
                      private val noteCollection: NoteCollection,
                      private val boardID: String,
                      private val activity: FragmentActivity)
    : FirebaseRecyclerAdapter<NoteCollection.Note, FirebaseAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.note_title_tv)
        val dateEdited = itemView.findViewById<TextView>(R.id.note_date_tv)

        fun bind(note: NoteCollection.Note){

            itemView.setOnLongClickListener {
                NoteActionDialog(note, noteCollection, boardID).show(activity.supportFragmentManager, NoteActionDialog.TAG)
                false
            }

            itemView.setOnClickListener {
                activity.findNavController(R.id.nav_host_fragment).navigate(NavigationGraphDirections.actionGlobalNoteDetail(
                        Json.encodeToString(note),
                        Json.encodeToString(noteCollection),
                        boardID
                ))
            }

            title.text = note.title
            dateEdited.text = note.getDateStr()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: NoteCollection.Note) {
        holder.bind(model)
    }
}