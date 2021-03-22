package com.example.swoosh.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.swoosh.R
import com.example.swoosh.data.model.Convo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatWindow : Fragment() {

    val args: ChatWindowArgs by navArgs()
    private val convo: Convo by lazy {
        Json.decodeFromString(args.convo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_window, container, false)
    }
}