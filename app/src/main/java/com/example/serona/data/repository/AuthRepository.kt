package com.example.serona.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth
){


    fun getCurrentUser() = auth.currentUser

    suspend fun getIdToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        return user.getIdToken(true).await().token
    }

    fun getCurrentUserInfo(): Pair<String?, String?>? {
        val user = auth.currentUser ?: return null

        return Pair(
            user.displayName,
            user.email
        )
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

    fun updateFirebaseName(newName: String) {
        val user = auth.currentUser
        val update = userProfileChangeRequest {
            displayName = newName
        }
        user?.updateProfile(update) // Update di background
    }

    fun reAuthenticate(password: String, callback: (Result<Unit>) -> Unit) {
        val user = auth.currentUser
        val email = user?.email ?: return

        // Kita buat kredensial login ulang
        val credential = EmailAuthProvider.getCredential(email, password)

        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(Result.success(Unit))
            } else {
                callback(Result.failure(task.exception ?: Exception("Password Salah")))
            }
        }
    }
}