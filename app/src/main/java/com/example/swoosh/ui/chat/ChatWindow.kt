package com.example.swoosh.ui.chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.MainActivity
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Convo
import com.example.swoosh.data.model.Message
import com.example.swoosh.ui.base.UserUriViewModel
import com.example.swoosh.utils.themeColor
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_chat_window.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatWindow : Fragment() {

    val args: ChatWindowArgs by navArgs()
    private val convo: Convo by lazy {
        Json.decodeFromString(args.convo)
    }
    private val viewModel: UserUriViewModel by activityViewModels()

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
        if (convo.name.substringAfter(":") == resources.getString(R.string.anonymous_board)){
            chat_window_title_tv.text = convo.name.substringBefore(":")
        }
        else{
            chat_window_title_tv.text = convo.name
        }

        val options = FirebaseRecyclerOptions.Builder<Message>()
                .setLifecycleOwner(viewLifecycleOwner)
                .setQuery(Repository.getConvoStoreQuery(convo.id), Message::class.java)
                .build()

        message_recycler.apply {
            adapter = object: MessageAdapter(options, requireActivity(), convo.id, viewModel){
                override fun onDataChanged() {
                    message_recycler.layoutManager?.smoothScrollToPosition(message_recycler, null, itemCount)
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
        (requireActivity() as MainActivity).emptyChatET()
        findNavController().navigateUp()
    }
}