package com.example.swoosh.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.NavigationGraphDirections
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.ui.base.ScrollListener
import com.example.swoosh.ui.chat.ChatViewModel
import com.example.swoosh.utils.PolySeri
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
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Home : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()
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

        //fetch chats for search
        chatViewModel.fetchConvos()

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //return transition
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        //set Transitions
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        home_reload_btn.setOnClickListener{
            viewModel.fetchBoards()
            Repository.fetchUser(Firebase.auth.currentUser?.email.toString(), requireActivity().baseContext)
        }

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
            Status.FAILED -> {
                home_progress_bar.isVisible = false
                home_status_failed.visibility = View.VISIBLE
                home_status_empty.visibility = View.GONE
            }
            Status.LOADING -> {
                home_progress_bar.isVisible = true
                home_status_failed.visibility = View.GONE
                home_status_empty.visibility = View.GONE
            }
            Status.SUCCESS -> {
                home_progress_bar.isVisible = false
                home_status_failed.visibility = View.GONE
                home_status_empty.visibility = View.GONE
            }
            Status.EMPTY -> {
                home_status_empty.visibility = View.VISIBLE
                home_progress_bar.isVisible = false
                home_status_failed.visibility = View.GONE
            }
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