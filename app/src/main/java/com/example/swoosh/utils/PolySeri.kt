package com.example.swoosh.utils

import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.data.model.NoteCollection
import com.example.swoosh.data.model.Todolist
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object PolySeri {
    private val boardItemsModule = SerializersModule {
        polymorphic(BoardItem::class){
            subclass(Todolist::class)
            subclass(NoteCollection::class)
        }
    }

    val json = Json{
        serializersModule = boardItemsModule
    }
}