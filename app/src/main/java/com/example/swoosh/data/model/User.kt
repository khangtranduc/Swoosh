package com.example.swoosh.data.model

import android.net.Uri
import kotlinx.serialization.Serializable

data class User(
        var name: String = "",
        var age: Long = -1,
        var from: String = "",
        var handle: String = "",
        var uri: Uri? = null
) {
}