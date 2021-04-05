package com.example.swoosh.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swoosh.data.model.NoteCollection

class NoteViewModel : ViewModel() {
    private val _note = MutableLiveData<NoteCollection.Note>()

    val note: LiveData<NoteCollection.Note>
        get() = _note

    fun updateNote(note: NoteCollection.Note){
        _note.value = note
    }
}