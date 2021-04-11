package com.example.swoosh.ui.chat

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swoosh.data.Repository
import com.example.swoosh.data.model.Board
import com.example.swoosh.data.model.Convo
import com.example.swoosh.utils.Status
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel(){
    private val _convos = MutableLiveData<ArrayList<Convo>>()
    private val _status = MutableLiveData(Status.LOADING)

    val convos: LiveData<ArrayList<Convo>>
        get() = _convos
    val status: LiveData<Status>
        get() = _status

    fun fetchConvos(){
        _convos.value?.let { if (it.isEmpty()) {_status.value = Status.LOADING } }
        Repository.getConvoRef().get().addOnSuccessListener {
            val toConvos = object: GenericTypeIndicator<Map<String, Convo>>(){}

            val convosQuery = it.getValue(toConvos)

            Repository.getKeysRef().get().addOnSuccessListener { keysSnapshot ->
                val toKeys = object: GenericTypeIndicator<Map<String, Boolean>>(){}

                val keysQuery = keysSnapshot.getValue(toKeys)
                val keys = keysQuery?.toSortedMap()

                if (keys != null && convosQuery != null){
                    val returnArray = arrayListOf<Convo>()
                    for ((key, value) in keys){
                        if (value){
                            convosQuery[key]?.let { it1 -> returnArray.add(it1) }
                        }
                    }

                    _convos.value = returnArray
                    if (returnArray.isNotEmpty()){
                        _status.value = Status.SUCCESS
                    }
                    else{
                        _status.value = Status.EMPTY
                    }
                }
            }.addOnFailureListener{
                _convos.value?.let { value -> if (value.isEmpty()) {_status.value = Status.FAILED} else {_status.value = Status.SUCCESS } }
            }
        }.addOnFailureListener{
            _convos.value?.let { value -> if (value.isEmpty()) {_status.value = Status.FAILED} else {_status.value = Status.SUCCESS } }
        }
    }
}