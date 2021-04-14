package com.example.swoosh.data

import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swoosh.MainApplication
import com.example.swoosh.R
import com.example.swoosh.data.model.*
import com.example.swoosh.utils.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

object Repository {
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    const val IMAGE_REQUEST = 10
    const val SPEECH_INPUT = 69

    fun getUserDir(email: String) : String{
        return "${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}"
    }

    fun putUserImageInStorage(uri: Uri, context: Context){
        Firebase.storage.reference.child("userImage/${getUserDir(Firebase.auth.currentUser?.email.toString())}").putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Image Failed to Upload", Toast.LENGTH_SHORT).show()
            }
    }

    fun getUserImageRef(email: String) : StorageReference{
        return Firebase.storage.reference.child("userImage/${getUserDir(email)}")
    }

    fun updateUserParticulars(user: User, email: String){
        _user.value = user
        val userRef = Firebase.database.reference
                .child("users").child("${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}")

        userRef.child("age").setValue(user.age)
        userRef.child("name").setValue(user.name)
        userRef.child("from").setValue(user.from)

        //update thing from chat
        updateConvoAftNameChange(user.name)
    }

    private fun updateConvoAftNameChange(newName: String){
        getKeysRef().get().addOnSuccessListener {
            val to = object: GenericTypeIndicator<Map<String, Boolean>>(){}

            val keys = it.getValue(to)
            keys?.let { keys ->
                for ((key, value) in keys){
                    if (value){
                        getConvoRef().child(key).get().addOnSuccessListener { snapshot ->
                            val convo = snapshot.getValue(Convo::class.java)

                            convo?.let {
                                if (convo.lastSenderEmail == Firebase.auth.currentUser?.email){
                                    getConvoRef().child(key).child("lastMessage")
                                            .setValue("$newName:${convo.lastMessage.substringAfter(":")}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchUser(email: String){
        Firebase.database.reference
                .child("users").child("${email.substringBefore("@")}_${email.substringBefore(".").substringAfter("@")}_${email.substringAfter(".")}")
                .get().addOnSuccessListener {
                    val temp_user = User().apply {
                        name = it.child("name").value as String
                        age = it.child("age").value as Long
                        from = it.child("from").value as String
                    }

                    getUserImageRef(email).downloadUrl.addOnSuccessListener {
                        temp_user.uri = it

                        _user.value = temp_user
                    }.addOnFailureListener {
                        _user.value = temp_user
                    }
                }
    }

    fun updateFBItem(item: FBItem, boardID: String){

        Firebase.database.reference.child("itemStore")
                .child(boardID).child("${item.dateCreated}")
                .child("name").setValue(item.name)
    }

    fun deleteFBItem(item: FBItem, boardID: String){

        Firebase.database.reference.child("itemStore")
                .child(boardID).child("${item.dateCreated}")
                .removeValue()
    }

    fun pushToFBItem(item: FBItem, containable: FBItem.Containable, boardID: String){

        Firebase.database.reference.child("itemStore")
                .child(boardID).child(item.dateCreated.toString())
                .child("containables").child(getContainableTag(item, containable))
                .setValue(containable)
    }

    fun fromCurrentUser(message: Message) : Boolean{
        return Firebase.auth.currentUser?.let { message.senderEmail == it.email } ?: false
    }

    fun updateToFBItem(item: FBItem, containableOld: FBItem.Containable, containableNew: FBItem.Containable, boardID: String){

        deleteFromFBItem(item, containableOld, boardID)

        pushToFBItem(item, containableNew, boardID)
    }

    fun deleteFromFBItem(item: FBItem, containable: FBItem.Containable, boardID: String){

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
                containable.date
            }
            else -> {
                ""
            }
        }
    }

    fun pushBoardItemToBoard(board: Board, item: FBItem){

        Firebase.database.reference.child("itemStore")
                .child(board.id)
                .child(item.dateCreated.toString()).setValue(item)
    }

    fun getBoardsRef() : DatabaseReference{
        return Firebase.database.reference.child("boards")
    }

    fun getKeysRef() : DatabaseReference{
        return Firebase.database.reference.child("users")
                .child(getUserDir(Firebase.auth.currentUser?.email.toString()))
                .child("keys")
    }

    fun getConvoRef() : DatabaseReference{
        return Firebase.database.reference.child("convos")
    }

    fun getItemRef(boardId: String): DatabaseReference {
        return Firebase.database.reference
                .child("itemStore")
                .child(boardId)
    }

    fun getConvoStoreQuery(convoID: String) : Query{
        return Firebase.database.reference.child("convoStore").child(convoID).limitToLast(50)
    }

    fun deleteBoard(board: Board){

        Firebase.database.reference.child("boards")
                .child(board.id).removeValue()

        getConvoRef().child(board.id).removeValue()

        Firebase.database.reference.child("convoStore")
                .child(board.id).removeValue()

        for ((key, value) in board.members){
            Firebase.database.reference.child("users")
                    .child(getUserDir(value.email)).child("keys").child(board.id).removeValue()
        }

        Firebase.database.reference.child("itemStore")
                .child(board.id).removeValue()
    }

    fun updateConvo(convo: Convo){
        getConvoRef().child(convo.id).child("name").setValue(convo.name)
    }

    fun deleteConvo(convoID: String){
        getConvoRef().child(convoID).removeValue()
    }

    fun updateBoard(board: Board){

        Firebase.database.reference.child("boards")
                .child(board.id).child("name").setValue(board.name)

        getConvoRef().child(board.id).child("name").setValue("${board.name}'s chat")
    }

    fun pushMessageToFirebase(message: Message, convoID: String){
        val client = Firebase.database.reference
                .child("convoStore")
                .child(convoID)
                .push()

        message.id = client.key.toString()

        client.setValue(message)
        getConvoRef().child(convoID).child("lastSenderEmail").setValue(message.senderEmail)
        getConvoRef().child(convoID).child("lastMessage").setValue("${message.sender}: ${message.message}")
    }

    fun deleteMessageFromFirebase(messageID: String, convoID: String, sender: String){
        Firebase.database.reference
                .child("convoStore")
                .child(convoID)
                .child(messageID).child("message").setValue(messageID)
        getConvoRef().child(convoID).child("lastMessage").setValue("${sender}: message deleted")
    }

    fun removeMemberFromBoard(boardID: String, email: String){
        getBoardsRef().child(boardID).child("members")
                .child(getUserDir(email)).removeValue()
        Firebase.database.reference.child("users")
                .child(getUserDir(email)).child("keys").child(boardID).removeValue()
    }

    fun pushQuickChatToFirebase(convo: Convo, membersCSV: String, context: Context){
        val client = getConvoRef().push()

        val reader = Scanner(membersCSV)
        reader.useDelimiter(",")

        val membersArray = arrayListOf<Board.Member>()

        while (reader.hasNext()){
            val email = reader.next().trim()

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
                    membersArray.remove(i)
                }
            }
        }

        convo.id = client.key.toString()
        convo.lastMessage = "No messages sent yet"

        client.setValue(convo).addOnSuccessListener {
            Toast.makeText(context, "Convo created successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context, "Convo failed to create", Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserRef(): DatabaseReference{
        return Firebase.database.reference.child("users")
    }

    fun addMemberToBoard(boardID: String, member: Board.Member, context: Context){
        if (member.email == Firebase.auth.currentUser?.email){
            Toast.makeText(context, "You can't add yourself", Toast.LENGTH_SHORT).show()
            return
        }
        try{
            getUserRef().child(getUserDir(member.email))
                    .get().addOnSuccessListener {
                        member.name = it.child("name").value as String
                        getBoardsRef().child(boardID).child("members")
                                .child(getUserDir(member.email)).setValue(member)
                        getUserRef().child(getUserDir(member.email)).child("keys")
                                .child(boardID).setValue(true)
                        Toast.makeText(context, "Member added sucessfully", Toast.LENGTH_SHORT).show()
                    }
        }
        catch (e: Error){
            Toast.makeText(context, "Member failed to add", Toast.LENGTH_SHORT).show()
        }
    }

    fun pushBoardToFirebase(board: Board, membersCSV: String, context: Context){
        val client = Firebase.database.reference
                .child("boards").push()

        val reader = Scanner(membersCSV)
        reader.useDelimiter(",")

        val membersArray = arrayListOf<Board.Member>()

        while (reader.hasNext()){
            val email = reader.next().trim()

            membersArray.add(Board.Member(email))
        }

        Firebase.auth.currentUser?.let {
            membersArray.add(Board.Member(it.email.toString()))
        }

        val usersRef = Firebase.database.reference.child("users")

        val mem_copy = membersArray.map { it }

        usersRef.get().addOnSuccessListener{
            val to = object: GenericTypeIndicator<HashMap<String, User>>(){}

            val users = it.getValue(to)

            for (i in mem_copy) {
                val userDir = getUserDir(i.email)
                if (it.hasChild(userDir)) {
                    usersRef.child(userDir).child("keys").child(client.key.toString()).setValue(true)
                    users?.let { users -> i.name = users[userDir]?.name.toString() }
                } else {
                    membersArray.remove(i)
                }
            }

            board.members = membersArray.map {member ->  getUserDir(member.email) to member }.toMap(HashMap())
            board.id = client.key.toString()

            if (board.members.size > 1){
                getConvoRef().child(board.id).setValue(Convo(board.id, "${board.name}'s chat", "No messages sent yet"))
            }

            client.setValue(board).addOnSuccessListener {
                Toast.makeText(context, "Board created successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Board failed to create", Toast.LENGTH_SHORT).show()
            }
        }
    }
}