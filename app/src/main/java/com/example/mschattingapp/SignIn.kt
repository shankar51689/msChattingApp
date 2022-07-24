package com.example.mschattingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mschattingapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignIn : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        mAuth   = FirebaseAuth.getInstance()

        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            btnSingUp.setOnClickListener {

                val name        = etName.text.toString()
                val email       = etEmail.text.toString()
                val password    = etPassword.text.toString()

                signUp(name, email, password)


            }
        }
    }

    private fun signUp(name: String, email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                addUserToDataBase(name, email, mAuth.currentUser?.uid!!)
                Log.d("TaG", "createUserWithEmail:success")
                val user = mAuth.currentUser
                finish()
                startActivity(Intent(this, MainActivity::class.java))

            } else {
                // If sign in fails, display a message to the user.
                Log.w("TaG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }

        }

    }

    private fun addUserToDataBase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name, email, uid))

    }

}