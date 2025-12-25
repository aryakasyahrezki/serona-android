package com.example.serona.ui.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(
//    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val auth : FirebaseAuth
){
    fun getCurrentUser() = auth.currentUser

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

    fun forgotPassword(
        email: String,
        callback: (Result<Unit>) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.success(Unit))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Unknown error")))
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

    fun emailVerification(
        callback: (Result<Unit>) -> Unit
    ){
        val user = auth.currentUser
            ?: return callback(Result.failure(Exception("User not found. Please register first.")))

        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    callback(Result.success(Unit))
                else
                    callback(Result.failure(task.exception ?: Exception("Unknown Error")))
            }

    }

    fun reloadUser(
        callback: (Boolean) -> Unit
    ){
        val user = auth.currentUser ?: return callback(false)

        user.reload()
            .addOnCompleteListener { task ->
                if(task.isSuccessful)
                    callback(user.isEmailVerified)
            }
    }

    fun logout() = auth.signOut()

    fun deleteCurrentUser(
        callback: (Result<Unit>) -> Unit
    ) {
        val user = auth.currentUser
            ?: return callback(Result.failure(Exception("User not logged in")))

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.success(Unit))
                } else {
                    callback(
                        Result.failure(
                            task.exception ?: Exception("Failed to delete user")
                        )
                    )
                }
            }
    }
}