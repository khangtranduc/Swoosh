package com.example.swoosh.data

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swoosh.data.model.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

object Repository {
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    fun getUserDir(email: String) : String{
        return "${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}"
    }

    fun updateUserParticulars(user: User, email: String){
        _user.value = user
        val userRef = Firebase.database.reference
                .child("users").child("${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}")

        userRef.child("age").setValue(user.age)
        userRef.child("name").setValue(user.name)
        userRef.child("from").setValue(user.from)
    }

    fun fetchUser(email: String){
        Firebase.database.reference
                .child("users").child("${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}")
                .get().addOnSuccessListener {
                    val temp_user = User()
                    temp_user.name = it.child("name").value as String
                    temp_user.age = it.child("age").value as Long
                    temp_user.from = it.child("from").value as String

                    _user.value = temp_user
                }
    }

    fun pushToFBItem(item: FBItem, containable: FBItem.Containable, boardID: String){
        Log.d("debug", "Add ${containable.name} to ${item.name}")

        Firebase.database.reference.child("itemStore")
                .child(boardID).child(item.dateCreated.toString())
                .child("containables").child(getContainableTag(item, containable))
                .setValue(containable)
    }

    fun deleteFBItem(item: FBItem, containable: FBItem.Containable, boardID: String){
        Log.d("debug", "Remove ${containable.name} from ${item.name}")

        Firebase.database.reference.child("itemStore")
                .child(boardID).child(item.dateCreated.toString())
                .child("containables").child(getContainableTag(item, containable))
                .removeValue()
    }

    private fun getContainableTag(item: FBItem, containable: FBItem.Containable) : String{
        return when (item.type) {
            "Todolist" -> {
                "${containable.details}_${containable.name}"
            }
            "NoteCollection" -> {
                //TODO: do something else here
                containable.name
            }
            else -> {
                ""
            }
        }
    }

    fun pushBoardItemToBoard(board: Board, item: FBItem){
        Log.d("debug", "Add $item to ${board.name}")

        Firebase.database.reference.child("itemStore")
                .child(board.id)
                .child(item.dateCreated.toString()).setValue(item)
    }

    fun getItemRef(boardId: String): DatabaseReference {
        return Firebase.database.reference
                .child("itemStore")
                .child(boardId)
                .child("items")
    }

    fun pushBoardToFirebase(board: Board, membersCSV: String, context: Context){
        val client = Firebase.database.reference
                .child("boards").push()

        val reader = Scanner(membersCSV)
        reader.useDelimiter(",")

        val membersArray = arrayListOf<Board.Member>()

        while (reader.hasNext()){
            val email = reader.next().trim()

            Log.d("debug", email)

            membersArray.add(Board.Member(email))
        }

        Firebase.auth.currentUser?.let {
            membersArray.add(Board.Member(it.email.toString()))
        }

        val usersRef = Firebase.database.reference.child("users")

        val mem_copy = membersArray.map { it.clone() }

        usersRef.get().addOnSuccessListener{
            for (i in mem_copy) {
                val userDir = "${i.email.substringBefore("@")}_${i.email.substringBefore(".").substringAfter("@")}_${i.email.substringAfter(".")}"
                if (it.hasChild(userDir)) {
                    usersRef.child(userDir).child("keys").child(client.key.toString()).setValue(true)
                } else {
                    Log.d("debug", "this is removed supossedly ${i.email}")
                    membersArray.remove(i)
                }
            }

            board.members = membersArray
            board.id = client.key.toString()

            Log.d("debug", board.members.toString())

            client.setValue(board).addOnSuccessListener {
                Toast.makeText(context, "Board created successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Board failed to create", Toast.LENGTH_SHORT).show()
            }
        }
    }
}