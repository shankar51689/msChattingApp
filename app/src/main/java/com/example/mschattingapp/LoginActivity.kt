package com.example.mschattingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mschattingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mAuth   = FirebaseAuth.getInstance()

        setClickListeners()
    }

    private fun setClickListeners() {
        val intentToSignIn = Intent(this,SignIn::class.java)
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        with(binding) {

            btnSingIn.setOnClickListener {
                startActivity(intentToSignIn)

            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TaG", "signInWithEmail:success")
                            val user = mAuth.currentUser
                            finish()
                            startActivity(intentToMainActivity)
//                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("mAuth", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                            updateUI(null)
                        }
                    }


            }
        }

    }

}