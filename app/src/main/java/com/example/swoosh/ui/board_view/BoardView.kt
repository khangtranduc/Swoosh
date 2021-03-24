package com.example.swoosh.ui.board_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class BoardView : Fragment() {

    val args: BoardViewArgs by navArgs()
    private val board: Board by lazy{
        Json.decodeFromString(args.board)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        board_view_toolbar.setupWithNavController(findNavController())
        board_view_toolbar.title = board.name
    }

}