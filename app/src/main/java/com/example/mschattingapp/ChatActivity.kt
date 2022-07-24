package com.example.mschattingapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mschattingapp.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private var receiverUid: String? = null
    private var senderUid: String? = null
    private var name: String? = null
    private lateinit var binding: ActivityMain2Binding
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var reciverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2)

        mDbRef = FirebaseDatabase.getInstance().reference

        setOnClickListners()
        getData()

        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(
            object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val msg =  postSnapshot.getValue(Message::class.java)
                        messageList.add(msg!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        supportActionBar?.title = name
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = messageAdapter
    }

    private fun getData() {
        name = intent.getStringExtra("name")
        receiverUid = intent.getStringExtra("uid")

        senderUid =  FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        reciverRoom = senderUid + receiverUid
    }

    private fun setOnClickListners() {


        binding.sendBtn.setOnClickListener {
             val msg = binding.etMessageBox.text.toString()
             val msgObj = Message(message = msg, senderId = senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObj).addOnSuccessListener {
                    mDbRef.child("chats").child(reciverRoom!!).child("messages").push()
                        .setValue(msgObj)
                }

            binding.etMessageBox.setText("")

        }
    }

}