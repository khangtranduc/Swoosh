package com.example.swoosh

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import com.example.swoosh.data.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //enables firebase data persistence only once
        Firebase.database.setPersistenceEnabled(true)
    }
}