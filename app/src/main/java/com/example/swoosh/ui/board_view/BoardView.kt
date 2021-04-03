package com.example.swoosh.ui.board_view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.ui.base.BoardItemCreationDialog
import com.example.swoosh.ui.todolist.TodolistFragment
import com.example.swoosh.utils.BoardUtils
import com.example.swoosh.utils.PolySeri
import kotlinx.android.synthetic.main.fab_add_sheet.*
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.serialization.decodeFromString

class BoardView : Fragment() {

    val args: BoardViewArgs by navArgs()
    private val board: Board by lazy{
        PolySeri.json.decodeFromString(args.board)
    }
    private lateinit var boardItemsFragments: ArrayList<Fragment>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        Log.d("debug", args.board)

        Log.d("debug", board.items.toString())

        boardItemsFragments = BoardUtils.getBoardItemFragments(board.items)

        return inflater.inflate(R.layout.fragment_board_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        board_view_toolbar.setupWithNavController(findNavController())
        board_view_toolbar.title = board.name

        Log.d("debug", boardItemsFragments.toString())

        board_content_viewpager.adapter = BoardPagerAdapter(childFragmentManager, boardItemsFragments)
    }

    fun pushTodolist(){
        BoardItemCreationDialog(true, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }

    fun pushNoteCollection(){
        BoardItemCreationDialog(false, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }
}