package com.example.swoosh.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.model.Convo

@SuppressLint("SetTextI18n")
class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val convos: ArrayList<Convo> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val primary = itemView.findViewById<TextView>(R.id.primary_search_tv)
        val secondary = itemView.findViewById<TextView>(R.id.secondary_search_tv)

        fun bind(convo: Convo){
            primary.text = convo.name
            secondary.text = convo.lastMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
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