package com.example.swoosh.ui.home

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.ui.board_view.BoardViewViewModel
import com.example.swoosh.utils.Status
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel() : ViewModel(){
    private val _boards = MutableLiveData<ArrayList<Board>>()
    private val _status = MutableLiveData(Status.LOADING)

    val boards : LiveData<ArrayList<Board>>
        get() = _boards
    val status : LiveData<Status>
        get() = _status

    fun fetchBoards(){
        Repository.getBoardsRef().get().addOnSuccessListener {
            val toBoards = object: GenericTypeIndicator<Map<String, Board>>(){}

            val boardsQuery = it.getValue(toBoards)

            Repository.getKeysRef().get().addOnSuccessListener { keysSnapshot ->
                val toKeys = object: GenericTypeIndicator<Map<String, Boolean>>(){}

                val keysQuery = keysSnapshot.getValue(toKeys)
                val keys = keysQuery?.toSortedMap()

                if (keys != null && boardsQuery != null){
                    val returnArray = arrayListOf<Board>()
                    for ((key, value) in keys){
                        if (value){
                            boardsQuery[key]?.let { it1 -> returnArray.add(it1) }
                        }
                    }

                    _boards.value = returnArray
                    if (returnArray.isNotEmpty()){
                        _status.value = Status.SUCCESS
                    }
                    else{
                        _status.value = Status.EMPTY
                    }
                }
            }.addOnFailureListener{
                _status.value = Status.FAILED
            }
        }.addOnFailureListener{
            _status.value = Status.FAILED
        }
    }

    fun clearBoards(){
        _status.value = Status.LOADING
        _boards.value = arrayListOf()
    }
}