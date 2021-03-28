package com.example.swoosh.ui.home

import android.annotation.SuppressLint
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class TestAdapter(var boards: List<Board>, var activity: FragmentActivity) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.board_title)
        val members = itemView.findViewById<TextView>(R.id.board_members)

        fun bind(board: Board){

            itemView.setOnClickListener{
                val action = HomeDirections.gotoBoardView(PolySeri.json.encodeToString(board))
                activity.findNavController(R.id.nav_host_fragment).navigate(action)
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
}