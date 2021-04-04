package com.example.swoosh.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.FBItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment() {


    private val boards = arrayListOf(
            Board("ja"),
            Board("pe"),
            Board("an"),
            Board("pi"),
            Board("le"),
            Board("fr"),
            Board("ha")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up firebase recycler things
        val keyRef = Firebase.database.reference
                .child("users")
                .child(Repository.getUserDir(Firebase.auth.currentUser?.email.toString()))
                .child("keys")

        val options = FirebaseRecyclerOptions.Builder<Board>()
                .setLifecycleOwner(viewLifecycleOwner)
                .setIndexedQuery(keyRef, Firebase.database.reference.child("boards"), Board::class.java)
                .build()

        home_recycler.apply {
            adapter = FirebaseAdapter(options, requireActivity())
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}