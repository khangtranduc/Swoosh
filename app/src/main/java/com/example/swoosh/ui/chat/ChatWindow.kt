package com.example.swoosh.ui.chat

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Convo
import com.example.swoosh.data.model.Message
import com.example.swoosh.utils.themeColor
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_board_view.*
import kotlinx.android.synthetic.main.fragment_chat_window.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatWindow : Fragment() {

    val args: ChatWindowArgs by navArgs()
    private val convo: Convo by lazy {
        Json.decodeFromString(args.convo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = requireContext().resources.getInteger(R.integer.motion_duration_long).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chat_window_up_btn.setOnClickListener {
            navigateUp()
        }
        chat_window_title_tv.text = convo.name

        val options = FirebaseRecyclerOptions.Builder<Message>()
                .setLifecycleOwner(viewLifecycleOwner)
                .setQuery(Repository.getConvoStoreQuery(convo.id), Message::class.java)
                .build()

        message_recycler.apply {
            adapter = object: MessageAdapter(options){
                override fun onDataChanged() {
                    message_recycler.scrollToPosition(itemCount)
                }
            }
            layoutManager = LinearLayoutManager(requireContext()).apply{
                stackFromEnd = true
            }
        }
    }

    fun getConvoId() : String{
        return convo.id
    }

    private fun navigateUp(){
        findNavController().navigateUp()
    }
}