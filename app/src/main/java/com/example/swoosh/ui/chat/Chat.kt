package com.example.swoosh.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Convo
import com.example.swoosh.ui.base.ScrollListener
import com.example.swoosh.ui.home.HomeAdapter
import com.example.swoosh.utils.Status
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_home.*

class Chat : Fragment() {

    private val convos = arrayListOf(
            Convo("", "Science Project", "gamer69: yes", 100),
            Convo("","Projectile Launcher", "gamer420: no", 100)
    )

    private val viewModel: ChatViewModel by activityViewModels()
    private val valueEventListener : ValueEventListener by lazy{
        object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.fetchConvos()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Repository.getConvoRef().addValueEventListener(valueEventListener)
    }

    override fun onStop() {
        super.onStop()
        Repository.getConvoRef().removeEventListener(valueEventListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //return transtions
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        //set Transitions
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        viewModel.convos.observe(viewLifecycleOwner){
            updateUI(it)
        }

        viewModel.status.observe(viewLifecycleOwner){
            updateStatus(it)
        }

        chat_reload_btn.setOnClickListener{
            viewModel.fetchConvos()
            Repository.fetchUser(Firebase.auth.currentUser?.email.toString(), requireActivity().baseContext)
        }

        convo_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(ScrollListener(requireActivity()))
        }
    }

    private fun updateStatus(status: Status){
        when(status){
            Status.FAILED -> {
                chat_progress_bar.isVisible = false
                chat_status_failed.visibility = View.VISIBLE
                chat_status_empty.visibility = View.GONE
            }
            Status.LOADING -> {
                chat_progress_bar.isVisible = true
                chat_status_failed.visibility = View.GONE
                chat_status_empty.visibility = View.GONE
            }
            Status.SUCCESS -> {
                chat_progress_bar.isVisible = false
                chat_status_failed.visibility = View.GONE
                chat_status_empty.visibility = View.GONE
            }
            Status.EMPTY -> {
                chat_status_empty.visibility = View.VISIBLE
                chat_progress_bar.isVisible = false
                chat_status_failed.visibility = View.GONE
            }
        }
    }

    private fun updateUI(convos: List<Convo>){
        if (convo_recycler.adapter == null){
            convo_recycler.adapter = ChatAdapter(requireActivity()).apply { submitList(convos) }
        }
        else{
            (convo_recycler.adapter as ChatAdapter).submitList(convos)
        }
    }
}