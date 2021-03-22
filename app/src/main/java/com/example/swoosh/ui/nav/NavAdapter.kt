package com.example.swoosh.ui.nav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.MainActivity
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.DrawerItem

class NavAdapter(var tabs: ArrayList<DrawerItem>, var activity: FragmentActivity) : RecyclerView.Adapter<NavAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tabText = itemView.findViewById<TextView>(R.id.tab_text)

        fun bind(drawer_item: DrawerItem){

            itemView.setOnClickListener{
                activity.findNavController(R.id.nav_host_fragment).navigate(drawer_item.destination)
            }

            tabText.text = drawer_item.title
            tabText.setCompoundDrawablesWithIntrinsicBounds(drawer_item.image, 0, 0, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.drawer_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tabs[position])
    }

    override fun getItemCount(): Int {
        return tabs.size
    }
}