package com.example.swoosh.ui.base

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
import com.example.swoosh.data.model.Convo
import com.example.swoosh.ui.chat.ChatDirections
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class SearchAdapter(var boards: List<Board>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.primary_search_tv)
        val members = itemView.findViewById<TextView>(R.id.secondary_search_tv)

        fun bind(board: Board){
            title.text = board.name
            members.text = "${board.members.size} members"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boards[position])
    }

    override fun getItemCount(): Int {
        return boards.size
    }
}