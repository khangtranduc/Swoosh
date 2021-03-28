package com.example.swoosh.data.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
abstract class BoardItem() {
    abstract var name: String
}