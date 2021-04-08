package com.example.swoosh.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.ui.base.ScrollListener
import com.example.swoosh.utils.Status
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment() {


//    private val boards = arrayListOf(
//            Board("ja"),
//            Board("pe"),
//            Board("an"),
//            Board("pi"),
//            Board("le"),
//            Board("fr"),
//            Board("ha")
//    )

    private val viewModel: HomeViewModel by activityViewModels()
    private val valueEventListener : ValueEventListener by lazy{
        object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.fetchBoards()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Repository.getBoardsRef().addValueEventListener(valueEventListener)
    }

    override fun onStop() {
        super.onStop()
        Repository.getBoardsRef().removeEventListener(valueEventListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //return transition
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        //setup enter and exit animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        //set up firebase recycler things
//        val keyRef = Firebase.database.reference
//                .child("users")
//                .child(Repository.getUserDir(Firebase.auth.currentUser?.email.toString()))
//                .child("keys")
//
//        val options = FirebaseRecyclerOptions.Builder<Board>()
//                .setLifecycleOwner(viewLifecycleOwner)
//                .setIndexedQuery(keyRef, Firebase.database.reference.child("boards"), Board::class.java)
//                .build()
//
//        val firebaseAdapter = FirebaseAdapter(options, requireActivity())

        home_recycler.apply{
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(ScrollListener(requireActivity()))
        }

        viewModel.boards.observe(viewLifecycleOwner){
            updateUI(it)
        }

        viewModel.status.observe(viewLifecycleOwner){
            updateStatus(it)
        }
    }

    private fun updateStatus(status: Status){
        when(status){
            Status.FAILED -> {}
            Status.LOADING -> {home_progress_bar.isVisible = true}
            Status.SUCCESS -> {home_progress_bar.isVisible = false}
            Status.EMPTY -> {}
        }
    }

    private fun updateUI(boards: ArrayList<Board>){
        if (home_recycler.adapter == null){
            home_recycler.adapter = HomeAdapter(requireActivity()).apply { submitList(boards) }
        }
        else{
            (home_recycler.adapter as HomeAdapter).submitList(boards)
        }
    }
}