package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MessagesAdapter(private val currentUserId: String) : Adapter<MessagesAdapter.MessagesViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 100
        private const val VIEW_TYPE_OTHER_MESSAGE = 101
    }

    private var messages: List<Message> = ArrayList()

    fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    class MessagesViewHolder(itemView: View) : ViewHolder(itemView) {
        val textViewMessage: TextView

        init {
            textViewMessage = itemView.findViewById(R.id.textViewMessage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val layoutResId = if (viewType == VIEW_TYPE_MY_MESSAGE) R.layout.my_message_item
        else R.layout.other_messages_item

        val view: View = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return MessagesViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.senderId == currentUserId) VIEW_TYPE_MY_MESSAGE
        else VIEW_TYPE_OTHER_MESSAGE
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]
        holder.textViewMessage.text = message.text
    }

    override fun getItemCount(): Int = messages.size
}