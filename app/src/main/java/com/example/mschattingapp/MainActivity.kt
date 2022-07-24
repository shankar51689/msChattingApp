package com.example.mschattingapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mschattingapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mAuth       = FirebaseAuth.getInstance()
        dbRef       = FirebaseDatabase.getInstance().reference
        userList    = ArrayList()

        adapter     = UserAdapter(this, userList)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter

        dbRef.child("user").addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.layout) {
            // logic for logout
            mAuth.signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
            return true
        }

        return true
    }

}