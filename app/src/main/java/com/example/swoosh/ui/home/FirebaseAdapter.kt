package com.example.swoosh.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.ui.dialog_fragments.BoardActionDialog
import com.example.swoosh.utils.PolySeri
import com.example.swoosh.utils.currentNavigationFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.platform.MaterialElevationScale
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

open class FirebaseAdapter(options: FirebaseRecyclerOptions<Board>,
                           private val activity: FragmentActivity)
    : FirebaseRecyclerAdapter<Board, FirebaseAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.board_title)
        val members = itemView.findViewById<TextView>(R.id.board_members)

        @SuppressLint("SetTextI18n")
        fun bind(board: Board){
            itemView.transitionName = activity.getString(R.string.board_card_transition_name, board.id)

            itemView.setOnClickListener{

                //add transitions
                activity.supportFragmentManager.currentNavigationFragment?.exitTransition = com.google.android.material.transition.MaterialElevationScale(false).apply {
                    duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                }
                activity.supportFragmentManager.currentNavigationFragment?.reenterTransition = com.google.android.material.transition.MaterialElevationScale(true).apply {
                    duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                }

                val boardViewTransitionName = activity.getString(R.string.board_view_transition_name)
                val extras = FragmentNavigatorExtras(itemView to boardViewTransitionName)
                val action = HomeDirections.gotoBoardView(Json.encodeToString(board))
                activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
            }

            itemView.setOnLongClickListener{
                BoardActionDialog(board).show(activity.supportFragmentManager, BoardActionDialog.TAG)
                false
            }

            title.text = board.name
            members.text = "${board.members.size} members"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Board) {
        holder.bind(model)
    }
}