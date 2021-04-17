package com.example.swoosh.ui.board_view

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.ui.base.BoardItemFragment
import com.example.swoosh.ui.dialog_fragments.AddMemberDialog
import com.example.swoosh.ui.dialog_fragments.BoardItemCreationDialog
import com.example.swoosh.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.board_item_overflow.*
import kotlinx.android.synthetic.main.fab_add_sheet.*
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_todolist.*
import kotlinx.android.synthetic.main.members_overflow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.reflect.Member
import java.util.*
import kotlin.collections.ArrayList

class BoardView : Fragment() {

    private val args: BoardViewArgs by navArgs()
    private val board: Board by lazy{
        Json.decodeFromString(args.board)
    }
    private lateinit var viewModel: BoardViewViewModel
    private val valueEventListener : ValueEventListener by lazy{
        object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.fetchBoardItems()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
    }
    private val memberEventListener: ValueEventListener by lazy {
        object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.fetchMembers()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = requireContext().resources.getInteger(R.integer.motion_duration_long).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = BoardViewModelFactory(board.id).create(BoardViewViewModel::class.java)

        return inflater.inflate(R.layout.fragment_board_view, container, false)
    }

    override fun onResume() {
        super.onResume()
        Repository.getItemRef(board.id).addValueEventListener(valueEventListener)
        Repository.getBoardsRef().child(board.id).child("members").addValueEventListener(memberEventListener)
    }

    override fun onStop() {
        super.onStop()
        Repository.getItemRef(board.id).removeEventListener(valueEventListener)
        Repository.getBoardsRef().child(board.id).child("members").removeEventListener(memberEventListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_board_item_btn.scaleX = 0f
        add_board_item_btn.scaleY = 0f
        lifecycleScope.launch{
            delay(300)
            add_board_item_btn.animate().scaleX(1f).scaleY(1f).setDuration(150).setInterpolator(AccelerateDecelerateInterpolator())
        }

        add_board_item_btn.setOnClickListener{
            lifecycleScope.launch {
                toggleBoardItemSheet()
            }
        }

        add_sheet_scrim.setOnClickListener{
            lifecycleScope.launch { toggleBoardItemSheet() }
        }

        add_todolist_btn.setOnClickListener{
            (requireActivity().supportFragmentManager.currentNavigationFragment as BoardView).pushTodolist()
            lifecycleScope.launch { toggleBoardItemSheet() }
        }

        add_notes_btn.setOnClickListener{
            (requireActivity().supportFragmentManager.currentNavigationFragment as BoardView).pushNoteCollection()
            lifecycleScope.launch { toggleBoardItemSheet() }
        }

        board_view_up_btn.setOnClickListener{
            navigateUp()
        }
        board_view_title_tv.text = board.name

        board_view_members.setOnClickListener {
            lifecycleScope.launch { toggleMemberSheet() }
        }

        members_overflow_scrim.setOnClickListener {
            lifecycleScope.launch { toggleMemberSheet() }
        }

        viewModel.boardItems.observe(viewLifecycleOwner) {
            updateBoardFragments(it)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            updateStatus(it)
        }

        viewModel.members.observe(viewLifecycleOwner){
            updateMembers(it)
        }

        board_view_reload_btn.setOnClickListener{
            viewModel.fetchBoardItems()
        }
        members_overflow_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        add_member_button.setOnClickListener {
            AddMemberDialog(board, requireContext()).show(childFragmentManager, AddMemberDialog.TAG)
        }
    }

    fun updateStatus(status: Status){
        when(status){
            Status.LOADING -> {
                board_view_progress_bar.visibility = View.VISIBLE
                board_view_status_failed.visibility = View.GONE
                board_view_status_empty.visibility = View.GONE
            }

            Status.EMPTY -> {
                board_view_progress_bar.visibility = View.GONE
                board_view_status_failed.visibility = View.GONE
                board_view_status_empty.visibility = View.VISIBLE
            }

            Status.SUCCESS -> {
                board_view_progress_bar.visibility = View.GONE
                board_view_status_failed.visibility = View.GONE
                board_view_status_empty.visibility = View.GONE
            }

            Status.FAILED -> {
                board_view_progress_bar.visibility = View.GONE
                board_view_status_failed.visibility = View.VISIBLE
                board_view_status_empty.visibility = View.GONE
            }
        }
    }

    private fun toggleBoardItemSheet(){
        val views = listOf<View>(add_board_item_btn, fab_add_card).sortedBy { !it.isVisible }
        val transition = MaterialContainerTransform().apply {
            startView = views.first()
            endView = views.last()
            duration = 300
            addTarget(views.last())
            setPathMotion(MaterialArcMotion())
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(board_view_container, transition)
        views.first().isVisible = false
        views.last().isVisible = true
        add_sheet_scrim.isVisible = !add_sheet_scrim.isVisible
    }

    private fun toggleMemberSheet(){
        val views = listOf<View>(board_view_members, members_overflow_card).sortedBy { !it.isVisible }
        val transition = MaterialContainerTransform().apply {
            startView = views.first()
            endView = views.last()
            duration = 300
            addTarget(views.last())
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(board_view_container, transition)
        views.first().isVisible = false
        views.last().isVisible = true
        members_overflow_scrim.isVisible = !members_overflow_scrim.isVisible

    }

    fun pushTodolist(){
        BoardItemCreationDialog(true, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }

    fun pushNoteCollection(){
        BoardItemCreationDialog(false, board).show(childFragmentManager, BoardItemCreationDialog.TAG)
    }

    fun navigateUp(){
        add_board_item_btn.animate().scaleY(0f).scaleX(0f).setDuration(150).setInterpolator(AccelerateInterpolator())
        findNavController().navigateUp()
    }

    private fun updateMembers(members: HashMap<String, Board.Member>){
        members_overflow_recycler.adapter = MemberAdapter(board.id, requireActivity().baseContext).apply { submitList(members) }
    }

    private fun updateBoardFragments(boardItems: SortedMap<String, BoardItem>?){
        val boardItemsFragments = boardItems?.let { BoardUtils.getBoardItemFragments(it, board.id) }
                ?: arrayListOf()

        if (board_content_viewpager.adapter != null){
            (board_content_viewpager.adapter as BoardPagerAdapter).submitList(boardItemsFragments)
        }
        else{
            board_content_viewpager.adapter = BoardPagerAdapter(requireActivity()).apply { submitList(boardItemsFragments) }
            TabLayoutMediator(board_view_dot_indicator, board_content_viewpager){_, _ -> }.attach()
        }
    }

}