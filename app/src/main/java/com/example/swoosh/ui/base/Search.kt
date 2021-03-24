package com.example.swoosh.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swoosh.R
import com.example.swoosh.data.model.Board
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_settings.*

class Search : Fragment() {

    private val boards = arrayListOf<Board>(
            Board("ja", arrayListOf()),
            Board("an", arrayListOf()),
            Board("ha", arrayListOf())
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_toolbar.setupWithNavController(findNavController())
        showSoftKeyboard()

        search_recycler.apply{
            adapter = SearchAdapter(boards)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showSoftKeyboard() {
        if (search_et.requestFocus()) {
            val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(search_et, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}