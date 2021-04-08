package com.example.swoosh.ui.home

import android.annotation.SuppressLint
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class HomeAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val boards: ArrayList<Board> = arrayListOf()

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
                val action = HomeDirections.gotoBoardView(PolySeri.json.encodeToString(board))
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
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.board_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boards[position])
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    fun submitList(newBoards: List<Board>){
        boards.apply {
            clear()
            addAll(newBoards)
            notifyDataSetChanged()
        }
    }
}