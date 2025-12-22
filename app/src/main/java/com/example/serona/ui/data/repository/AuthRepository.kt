package com.example.serona.ui.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import java.lang.Exception

class AuthRepository (
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
){
    fun isLoggedIn(): Boolean {
        val user = auth.currentUser ?: return false
        return user.isEmailVerified || true // sementara
    }

    fun login(
        email : String,
        password : String,
        callback : (Result<Unit>) -> Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    callback(Result.success(Unit))
                }else{
                    callback(Result.failure(task.exception ?: Exception("Unknown Error")))
                }
            }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (Result<Unit>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    callback(Result.failure(task.exception ?: Exception("Unknown error")))
                    return@addOnCompleteListener
                }

                // update nama user
                val update = userProfileChangeRequest {
                    displayName = name
                }
                auth.currentUser?.updateProfile(update)

                callback(Result.success(Unit))
            }
    }

    fun logout() = auth.signOut()
}