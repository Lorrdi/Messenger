package com.example.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Users")

    val error: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val user: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>()
    }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let { firebaseUser ->
                user.value = firebaseUser
            }
        }
    }

    fun signUp(
        email: String, password: String, name: String, lastName: String, age: Int
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            val firebaseUser = authResult.user
            val user = if (firebaseUser != null) User(
                firebaseUser.uid, name, lastName, age, false
            ) else return@addOnSuccessListener
            usersRef.child(user.id).setValue(user)
        }.addOnFailureListener {
            error.value = it.message
        }
    }
}