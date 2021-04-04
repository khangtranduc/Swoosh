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
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.ui.dialog_fragments.BoardItemCreationDialog
import com.example.swoosh.utils.BoardUtils
import com.example.swoosh.utils.PolySeri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.serialization.decodeFromString
import java.util.*
import kotlin.collections.ArrayList

class BoardView : Fragment() {

    val args: BoardViewArgs by navArgs()
    private val board: Board by lazy{
        PolySeri.json.decodeFromString(args.board)
    }
    private lateinit var viewModel: BoardViewViewModel
    private lateinit var boardItemsFragments: ArrayList<Fragment>
    private val valueEventListener : ValueEventListener by lazy{
        object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.fetchBoardItems()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = BoardViewModelFactory(board.id).create(BoardViewViewModel::class.java)

        updateBoardFragments(viewModel.boardItems.value)

        Repository.getItemRef(board.id).addValueEventListener(valueEventListener)

        return inflater.inflate(R.layout.fragment_board_view, container, false)
    }

    override fun onStop() {
        super.onStop()
        Repository.getItemRef(board.id).removeEventListener(valueEventListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        board_view_toolbar.setupWithNavController(findNavController())
        board_view_toolbar.title = board.name

        viewModel.boardItems.observe(viewLifecycleOwner){
            updateBoardFragments(it)
            board_content_viewpager.adapter = BoardPagerAdapter(childFragmentManager, boardItemsFragments)
        }

        board_content_viewpager.adapter = BoardPagerAdapter(childFragmentManager, boardItemsFragments)
    }

    fun pushTodolist(){
        BoardItemCreationDialog(true, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }

    fun pushNoteCollection(){
        BoardItemCreationDialog(false, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }

    private fun updateBoardFragments(boardItems: SortedMap<String, BoardItem>?){
        boardItemsFragments = boardItems?.let { BoardUtils.getBoardItemFragments(it, board.id) }
                ?: arrayListOf()

        Log.d("debug", "$boardItemsFragments")
    }

}