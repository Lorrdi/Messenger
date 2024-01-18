package com.example.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChatViewModel(private val currentUserId: String, private val otherUserId: String) :
    ViewModel() {

    val messages: MutableLiveData<List<Message>> = MutableLiveData()
    val otherUser: MutableLiveData<User> = MutableLiveData()
    val messageSent: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("Users")
    private val messagesRef: DatabaseReference = database.getReference("Messages")

    init {
        usersRef.child(otherUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) otherUser.value = user
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        messagesRef.child(currentUserId).child(otherUserId).addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = ArrayList<Message>()
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messagesList.add(message)
                    }
                }
                messages.value = messagesList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun setUserOnline(isOnline: Boolean) {
        usersRef.child(currentUserId).child("online").setValue(isOnline)
    }
    fun sendMessage(message: Message) {
        messagesRef
            .child(message.senderId)
            .child(message.receiverId)
            .push()
            .setValue(message)
            .addOnSuccessListener { messagesRef
                    .child(message.receiverId)
                    .child(message.senderId)
                    .push()
                    .setValue(message).addOnSuccessListener {
                        messageSent.value = true
                }
            }
            .addOnFailureListener {
                error.value = it.message
            }
    }
}