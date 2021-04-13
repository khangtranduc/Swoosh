package com.example.swoosh.ui.chat

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Convo
import com.example.swoosh.ui.dialog_fragments.QuickChatActionDialog
import com.example.swoosh.utils.currentNavigationFragment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class ChatAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val convos: ArrayList<Convo> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.board_title)
        val lastMessage = itemView.findViewById<TextView>(R.id.board_members)
        val quick_chat_icon = itemView.findViewById<ImageView>(R.id.quick_chat_icon)

        fun bind(convo: Convo){
            itemView.transitionName = convo.name

            itemView.setOnClickListener{

                activity.supportFragmentManager.currentNavigationFragment?.exitTransition = com.google.android.material.transition.MaterialElevationScale(false).apply {
                    duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                }
                activity.supportFragmentManager.currentNavigationFragment?.reenterTransition = com.google.android.material.transition.MaterialElevationScale(true).apply {
                    duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                }

                val chatWindowTransitionName = activity.resources.getString(R.string.chat_window_transition_name)
                val extras = FragmentNavigatorExtras(itemView to chatWindowTransitionName)
                val action = ChatDirections.gotoChatWindow(Json.encodeToString(convo))
                activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
            }

            title.text = convo.name
            lastMessage.setTypeface(lastMessage.typeface, Typeface.ITALIC)
            lastMessage.text = convo.lastMessage

            if (convo.name.substringAfter(":") == activity.resources.getString(R.string.anonymous_board)){
                quick_chat_icon.visibility = View.VISIBLE
                title.text = convo.name.substringBefore(":")

                itemView.setOnLongClickListener {
                    QuickChatActionDialog(convo).show(activity.supportFragmentManager, QuickChatActionDialog.TAG)
                    false
                }
            }
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

    fun submitList(newConvos: List<Convo>){
        convos.apply {
            clear()
            addAll(newConvos)
            notifyDataSetChanged()
        }
    }
}