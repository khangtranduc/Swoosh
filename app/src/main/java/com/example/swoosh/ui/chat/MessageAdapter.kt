package com.example.swoosh.ui.chat

import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Message
import com.example.swoosh.ui.base.UserUriViewModel
import com.example.swoosh.ui.dialog_fragments.MessageDeleteDialog
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

open class MessageAdapter(options: FirebaseRecyclerOptions<Message>,
                          private val activity: FragmentActivity,
                          private val convoID: String, private val activityViewModel: UserUriViewModel
) : FirebaseRecyclerAdapter<Message, MessageAdapter.ViewHolder>(options) {


    private val otherUsersImageUri = hashMapOf<String, Uri>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val in_container = itemView.findViewById<ConstraintLayout>(R.id.in_message_container)
        val in_image = itemView.findViewById<CircleImageView>(R.id.in_message_img)
        val in_message = itemView.findViewById<TextView>(R.id.in_message_tv)
        val in_timeStamp = itemView.findViewById<TextView>(R.id.in_time_stamp_tv)
        val in_picture = itemView.findViewById<ImageView>(R.id.in_message_picture)

        val out_container = itemView.findViewById<ConstraintLayout>(R.id.out_message_container)
        val out_image = itemView.findViewById<CircleImageView>(R.id.out_message_img)
        val out_message = itemView.findViewById<TextView>(R.id.out_message_tv)
        val out_timeStamp = itemView.findViewById<TextView>(R.id.out_time_stamp_tv)
        val out_picture = itemView.findViewById<ImageView>(R.id.out_message_picture)

        fun bind(message: Message){

            if (Repository.fromCurrentUser(message)){
                in_container.visibility = View.GONE
                out_container.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(Repository.user.value?.uri)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(out_image)

                if (message.message == message.id){
                    out_message.setTypeface(out_message.typeface, Typeface.ITALIC)
                    out_message.text = "message deleted "
                }
                else if (message.message.substringBefore(":") == activity.resources.getString(R.string.anonymous_board)){
                    out_message.visibility = View.GONE
                    out_picture.visibility = View.VISIBLE
                    Repository.getConvoImageRef(convoID, message.message.substringAfter(":")).downloadUrl.addOnSuccessListener {
                        Glide.with(activity)
                                .load(it)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(out_picture)
                    }
                            .addOnFailureListener{
                                out_message.visibility = View.VISIBLE
                                out_picture.visibility = View.GONE
                                out_message.text = "Image Loading Failed"
                            }
                    itemView.setOnLongClickListener {
                        MessageDeleteDialog(message.id, convoID, message.sender,
                                true, message.message.substringAfter(":")).show(activity.supportFragmentManager, MessageDeleteDialog.TAG)
                        false
                    }
                }
                else{
                    out_message.text = message.message
                    itemView.setOnLongClickListener {
                        MessageDeleteDialog(message.id, convoID, message.sender).show(activity.supportFragmentManager, MessageDeleteDialog.TAG)
                        false
                    }
                }
                out_timeStamp.text = message.getTimeStampString()
            }
            else {
                in_container.visibility = View.VISIBLE
                out_container.visibility = View.GONE

                if (activityViewModel.otherUsersImageUri.containsKey(message.senderEmail)){

                    Glide.with(activity)
                        .load(activityViewModel.otherUsersImageUri[message.senderEmail])
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(in_image)
                }
                else{
                    Repository.getUserImageRef(message.senderEmail).downloadUrl.addOnSuccessListener {
                        Glide.with(activity)
                            .load(it)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(in_image)

                        activityViewModel.otherUsersImageUri[message.senderEmail] = it
                    }
                }

                if (message.message == message.id){
                    in_message.setTypeface(out_message.typeface, Typeface.ITALIC)
                    in_message.text = "message deleted "
                }
                else if (message.message.substringBefore(":") == activity.resources.getString(R.string.anonymous_board)){
                    in_message.visibility = View.GONE
                    in_picture.visibility = View.VISIBLE
                    Repository.getConvoImageRef(convoID, message.message.substringAfter(":")).downloadUrl.addOnSuccessListener {
                        Glide.with(activity)
                                .load(it)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(in_picture)
                    }
                            .addOnFailureListener{
                                in_picture.visibility = View.GONE
                                in_message.visibility = View.VISIBLE
                                in_message.text = "Image Loading Failed"
                            }
                }
                else{
                    in_message.text = message.message
                }
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