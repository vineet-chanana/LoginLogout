package com.example.loginlogout

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignupActivityViewModel(val repository: Repository) : ViewModel(){




    companion object {
        private const val RC_SIGN_IN = 120
    }

    var loginMLD = repository.loginMLD
    fun signInFromGoogle() {
        repository.createRequest()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        repository.onActivityResult(requestCode,resultCode,data)
    }

    fun signInFromTwitter() {
        repository.signInFromTwitter()
    }


}