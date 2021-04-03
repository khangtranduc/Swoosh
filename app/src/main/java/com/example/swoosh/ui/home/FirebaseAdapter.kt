package com.example.swoosh.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.utils.PolySeri
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.serialization.encodeToString

class FirebaseAdapter(options: FirebaseRecyclerOptions<Board>,
                      private val activity: FragmentActivity)
    : FirebaseRecyclerAdapter<Board, FirebaseAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.board_title)
        val members = itemView.findViewById<TextView>(R.id.board_members)

        @SuppressLint("SetTextI18n")
        fun bind(board: Board){

            itemView.setOnClickListener{
                val action = HomeDirections.gotoBoardView(PolySeri.json.encodeToString(board))
                activity.findNavController(R.id.nav_host_fragment).navigate(action)
            }

            itemView.setOnLongClickListener{

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