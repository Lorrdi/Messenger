package com.example.messenger


data class User(
    val id: String = "",
    val name: String = "",
    val lastName: String = "",
    val age: Int = 1,
    val online: Boolean = false
) {
    override fun toString(): String {
        return "User(id='$id', name='$name', lastName='$lastName', age=$age, isOnline=$online)"
    }
}
