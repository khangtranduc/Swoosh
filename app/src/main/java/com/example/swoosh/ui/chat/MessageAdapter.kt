package com.example.swoosh.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Message
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(options: FirebaseRecyclerOptions<Message>) : FirebaseRecyclerAdapter<Message, MessageAdapter.ViewHolder>(options) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val in_container = itemView.findViewById<ConstraintLayout>(R.id.in_message_container)
        val in_image = itemView.findViewById<CircleImageView>(R.id.in_message_img)
        val in_message = itemView.findViewById<TextView>(R.id.in_message_tv)
        val in_timeStamp = itemView.findViewById<TextView>(R.id.in_time_stamp_tv)

        val out_container = itemView.findViewById<ConstraintLayout>(R.id.out_message_container)
        val out_image = itemView.findViewById<CircleImageView>(R.id.out_message_img)
        val out_message = itemView.findViewById<TextView>(R.id.out_message_tv)
        val out_timeStamp = itemView.findViewById<TextView>(R.id.out_time_stamp_tv)

        fun bind(message: Message){
            Log.d("debug", message.message)
            if (Repository.fromCurrentUser(message)){
                in_container.visibility = View.GONE
                out_container.visibility = View.VISIBLE

                //TODO: implement out_image

                out_message.text = message.message
                out_timeStamp.text = message.getTimeStampString()
            }
            else {
                in_container.visibility = View.VISIBLE
                out_container.visibility = View.GONE

                //TODO: implement in_image

                in_message.text = message.message
                in_timeStamp.text = message.getTimeStampString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
        holder.bind(model)
    }
}