package com.example.swoosh.ui.home

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swoosh.data.model.Board
import com.example.swoosh.ui.board_view.BoardViewViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel() : ViewModel(){
    private val _boards = MutableLiveData<ArrayList<Board>>()

    val boards : LiveData<ArrayList<Board>>
        get() = _boards

    fun fetchBoards(){
        Firebase.database.reference
                .child("boards").get()
                .addOnSuccessListener {
                    val to = object: GenericTypeIndicator<List<Board>>(){}

                    val boardsQuery = it.getValue(to)

                    boardsQuery?.let{
                        val boardsFinal = arrayListOf<Board>()

                        for (i in boardsQuery){

                        }

                        _boards.value = ArrayList(boardsQuery)
                    }
                }
    }
}