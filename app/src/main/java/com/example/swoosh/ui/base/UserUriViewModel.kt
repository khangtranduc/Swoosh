package com.example.swoosh.ui.base

import android.net.Uri
import androidx.lifecycle.ViewModel

class UserUriViewModel : ViewModel() {
    val otherUsersImageUri = hashMapOf<String, Uri>()
}