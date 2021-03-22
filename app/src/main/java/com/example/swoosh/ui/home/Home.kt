package com.example.swoosh.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment() {

    private val boards = arrayListOf<Board>(
            Board("ja", arrayListOf()),
            Board("pe", arrayListOf()),
            Board("an", arrayListOf()),
            Board("pi", arrayListOf()),
            Board("le", arrayListOf()),
            Board("fr", arrayListOf()),
            Board("ha", arrayListOf())
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

        home_recycler.apply {
            adapter = TestAdapter(boards)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}