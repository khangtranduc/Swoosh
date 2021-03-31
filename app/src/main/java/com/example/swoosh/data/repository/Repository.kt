package com.example.swoosh.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swoosh.data.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_info.*
import kotlinx.serialization.descriptors.PrimitiveKind

object Repository {
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    fun updateUserParticulars(user: User, email: String){
        _user.value = user
        val userRef = Firebase.database.reference
                .child("users").child(email.substringBefore("@"))

        userRef.child("age").setValue(user.age)
        userRef.child("name").setValue(user.name)
        userRef.child("from").setValue(user.from)
    }

    fun fetchUser(email: String){
        Firebase.database.reference
                .child("users").child(email.substringBefore("@"))
                .get().addOnSuccessListener {
                    val temp_user = User()
                    temp_user.name = it.child("name").value as String
                    temp_user.age = it.child("age").value as Long
                    temp_user.from = it.child("from").value as String

                    _user.value = temp_user
                }
    }
}