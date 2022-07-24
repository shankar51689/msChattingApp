package com.example.mschattingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(val context: Context, val MessageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT       = 1
    val ITEM_RECEIVE    = 2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 2) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val msg = MessageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
                val viewHolder = holder as SentViewHolder
                holder.sentMsg.text = msg.message


        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receivedMsg.text = msg.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = MessageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(msg.senderId)) {
            ITEM_SENT
        } else ITEM_RECEIVE


    }

    override fun getItemCount(): Int {
        return MessageList.size
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var sentMsg = itemView.findViewById<TextView>(R.id.textSentMessage)
    }


    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var receivedMsg = itemView.findViewById<TextView>(R.id.textReceivedMessage)
    }
}

