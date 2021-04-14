package com.example.swoosh.ui.board_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.zip.Inflater

class MemberAdapter(private val boardID: String, private val context: Context) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    private val members = arrayListOf<Board.Member>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.member_name_tv)
        val email = itemView.findViewById<TextView>(R.id.member_mail_tv)
        val removeBtn = itemView.findViewById<ImageButton>(R.id.remove_member_btn)

        fun bind(member: Board.Member){
            name.text = member.name
            email.text= member.email

            if (email.text == Firebase.auth.currentUser?.email) {
                itemView.alpha = 0.5f
            }
            else{
                removeBtn.setOnClickListener {
                    Repository.removeMemberFromBoard(boardID, member.email, members.size, context)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.member_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount(): Int {
        return members.size
    }

    fun submitList(newMembers: Map<String, Board.Member>){
        members.apply {
            clear()
            addAll(newMembers.map{(_, value) -> value }.toList())
            notifyDataSetChanged()
        }
    }
}