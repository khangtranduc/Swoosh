package com.example.swoosh.ui.search

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.SearchItem
import com.example.swoosh.ui.chat.ChatViewModel
import com.example.swoosh.ui.home.HomeViewModel
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList

class Search : Fragment() {

    private val chatViewModel: ChatViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var searchItems: ArrayList<SearchItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ false)
    }

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        chatViewModel.fetchConvos()

        homeViewModel.boards.value?.let {
            searchItems.clear()
            searchItems.addAll(it.map { item -> item })
        }

        chatViewModel.convos.value?.let {
            searchItems.addAll(it.map { item -> item })
        }

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw {
            showSoftKeyboard()
        }

        search_up_btn.setOnClickListener {
            hideSoftKeyboard()
            findNavController().navigateUp()
        }

        mic_btn.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
            {
                this.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 10)
            }
            else{
                listen()
            }
        }

        search_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = arrayListOf<SearchItem>()
                for (i in searchItems) {
                    if (i.name.toLowerCase().matches("${s.toString().toLowerCase()}.*".toRegex())){
                        filteredList.add(i)
                    }
                }
                (search_recycler.adapter as SearchAdapter).apply {
                    submitList(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        search_recycler.apply{
            adapter = SearchAdapter(requireActivity()).apply { submitList(searchItems) }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun hideSoftKeyboard(){
        val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showSoftKeyboard() {
        if (search_et.requestFocus()) {
            val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(search_et, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun listen(){
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")

        try{
            startActivityForResult(i, Repository.SPEECH_INPUT)
        }
        catch (e: ActivityNotFoundException){
            Toast.makeText(requireContext(), "the thing isn't supported lmao", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Repository.SPEECH_INPUT){
            if (resultCode == Activity.RESULT_OK && data != null){
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
                val body = result[0]
                search_et.setText(body)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            10 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(),
                            "Recording Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show()
                    listen()
                } else {
                    Toast.makeText(requireContext(),
                            "Recording Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }
}