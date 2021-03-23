package com.example.swoosh.ui.chat

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
import com.example.swoosh.data.model.Convo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class TestAdapter(var convos: List<Convo>, var activity: FragmentActivity) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.board_title)
        val members = itemView.findViewById<TextView>(R.id.board_members)

        fun bind(convo: Convo){
            itemView.setOnClickListener{
                val action = ChatDirections.gotoChatWindow(Json.encodeToString(convo))
                activity.findNavController(R.id.nav_host_fragment).navigate(action)
            }

            title.text = convo.name
            members.text = convo.lastMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.board_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(convos[position])
    }

    override fun getItemCount(): Int {
        return convos.size
    }
}