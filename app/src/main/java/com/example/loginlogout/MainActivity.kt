package com.example.loginlogout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth


open class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: SignupActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this,SignupActivityViewModelFactory(Repository(this))).get(SignupActivityViewModel::class.java)


        val btnGoogle: ImageView = findViewById(R.id.btnGoogle)
        btnGoogle.setOnClickListener {
            viewModel.signInFromGoogle()
        }

        val btnTwitter: ImageView = findViewById(R.id.btnTwitter)
        btnTwitter.setOnClickListener {
            viewModel.signInFromTwitter()
        }

        viewModel.loginMLD.observe(this) {
            it?.let {  loggedIn->
                if(loggedIn){
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode,resultCode,data)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            Log.d("onStart","Loggedin via onStart")
            finish()
        }
    }



}