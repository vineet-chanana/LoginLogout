package com.example.loginlogout

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

class Repository(val activity: Activity)  {


    private lateinit var googleSignInClient: GoogleSignInClient
    private var mAuth = FirebaseAuth.getInstance()
    var loginMLD = MutableLiveData<Boolean>()


    companion object {
        private const val RC_SIGN_IN = 120
    }
     fun createRequest() {
        //in here we request google to give us all email options
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)

         signIn()
    }

    private fun signIn() {
        //when we click on sign in button, we will open up the pop to display all email options
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, Repository.RC_SIGN_IN)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Repository.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    //authenticate the user with firebase
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    loginMLD.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }


    fun signInFromTwitter() {

        val provider = OAuthProvider.newBuilder("twitter.com")
        // Target specific email with login hint.
        provider.addCustomParameter("lang", "fr");

        val pendingResultTask: Task<AuthResult>? = mAuth.getPendingAuthResult()
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        loginMLD.value = true
                        Log.v("TwitterActivity0", it.user.toString())
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        Log.v("TwitterActivity0", it.message.toString())
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    })
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            mAuth
                .startActivityForSignInWithProvider( /* activity= */activity, provider.build())
                .addOnSuccessListener {
                    Log.v("TwitterActivity", it.user.toString())
                    loginMLD.value = true
                }
                .addOnFailureListener {
                    // Handle failure.
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    Log.v("TwitterActivity", it.message.toString())
                }
        }
    }



}