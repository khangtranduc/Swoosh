package com.example.swoosh.ui.board_view

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.FBItem
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class BoardViewViewModel(private val boardID: String) : ViewModel() {
    private val _boardItems = MutableLiveData<SortedMap<String, BoardItem>>()
    var pagerState: Int = -1

    val boardItems : LiveData<SortedMap<String, BoardItem>>
        get() = _boardItems

    fun fetchBoardItems(){
        Firebase.database.reference
                .child("itemStore")
                .child(boardID)
                .get().addOnSuccessListener {
                    val to = object: GenericTypeIndicator<Map<String, FBItem>>(){}

                    val itemStoreQuery = it.getValue(to)
                    val itemStore = itemStoreQuery?.toSortedMap()

                    Log.d("debug", "${itemStore}")

                    itemStore?.let { _boardItems.value = Board.getActualItems(itemStore) }
                }
    }
}

class BoardViewModelFactory(private val boardID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardViewViewModel(boardID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}