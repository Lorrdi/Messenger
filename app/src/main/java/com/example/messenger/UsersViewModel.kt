package com.example.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersViewModel : ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("Users")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            user.value = firebaseAuth.currentUser
        }
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = auth.currentUser ?: return
                val usersFromDB = ArrayList<User>()
                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(User::class.java) ?: return

                    if (user.id != currentUser.uid) {
                        usersFromDB.add(user)
                    }

                }
                users.value = usersFromDB
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        
    }
    fun setUserOnline(isOnline: Boolean) {
        val firebaseUser = auth.currentUser ?: return
        usersRef.child(firebaseUser.uid).child("online").setValue(isOnline)
    }
    fun logout() {
        setUserOnline(false)
        auth.signOut()
    }
}