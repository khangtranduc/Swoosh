package com.example.swoosh.ui.search

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.Convo
import com.example.swoosh.data.model.SearchItem
import com.example.swoosh.ui.chat.ChatDirections
import com.example.swoosh.ui.home.HomeDirections
import com.example.swoosh.utils.currentNavigationFragment
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SetTextI18n")
class SearchAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val searchItems: ArrayList<SearchItem> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val primary = itemView.findViewById<TextView>(R.id.primary_search_tv)
        val secondary = itemView.findViewById<TextView>(R.id.secondary_search_tv)

        fun bind(searchItem: SearchItem){

            if (searchItem is Board){
                primary.text = searchItem.name
                secondary.text = "${searchItem.members.size} members"
                itemView.transitionName = activity.resources.getString(R.string.board_card_transition_name, searchItem.id)

                itemView.setOnClickListener{

                    //add transitions
                    activity.supportFragmentManager.currentNavigationFragment?.exitTransition = MaterialElevationScale(false).apply {
                        duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                    }
                    activity.supportFragmentManager.currentNavigationFragment?.reenterTransition = MaterialElevationScale(true).apply {
                        duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                    }

                    val boardViewTransitionName = activity.getString(R.string.board_view_transition_name)
                    val extras = FragmentNavigatorExtras(itemView to boardViewTransitionName)
                    val action = SearchDirections.gotoBoardView(Json.encodeToString(searchItem))
                    activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
                }
            }
            else if (searchItem is Convo){
                secondary.text = searchItem.lastMessage
                secondary.setTypeface(secondary.typeface, Typeface.ITALIC)
                itemView.transitionName = searchItem.name

                if (searchItem.name.substringAfter(":") == activity.resources.getString(R.string.anonymous_board)){
                    primary.text = searchItem.name.substringBefore(":")
                }
                else{
                    primary.text = searchItem.name
                }

                itemView.setOnClickListener{

                    activity.supportFragmentManager.currentNavigationFragment?.exitTransition = MaterialElevationScale(false).apply {
                        duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                    }
                    activity.supportFragmentManager.currentNavigationFragment?.reenterTransition = MaterialElevationScale(true).apply {
                        duration = activity.baseContext.resources.getInteger(R.integer.motion_duration_long).toLong()
                    }

                    val chatWindowTransitionName = activity.resources.getString(R.string.chat_window_transition_name)
                    val extras = FragmentNavigatorExtras(itemView to chatWindowTransitionName)
                    val action = SearchDirections.gotoChatWindow(Json.encodeToString(searchItem))
                    activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchItems[position])
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    fun submitList(newSearchItems: List<SearchItem>){
        searchItems.apply {
            clear()
            addAll(newSearchItems)
            notifyDataSetChanged()
        }
    }
}