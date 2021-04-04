package com.example.swoosh.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.model.Convo
import com.example.swoosh.ui.base.ScrollListener
import kotlinx.android.synthetic.main.fragment_chat.*

class Chat : Fragment() {

    private val convos = arrayListOf(
            Convo("Science Project", "gamer69: yes", 100),
            Convo("Projectile Launcher", "gamer420: no", 100)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        convo_recycler.apply {
            adapter = TestAdapter(convos, requireActivity())
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(ScrollListener(requireActivity()))
        }
    }
}