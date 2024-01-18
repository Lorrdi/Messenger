package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class UsersAdapter : Adapter<UsersAdapter.UsersViewHolder>() {

    private var users: List<User> = ArrayList()
    private lateinit var onUserClickListener: OnUserClickListener

    fun setOnUserClickListener(onUserClickListener: OnUserClickListener) {
        this.onUserClickListener = onUserClickListener
    }

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class UsersViewHolder(itemView: View) : ViewHolder(itemView) {

        val textViewUserInfo: TextView
        val userOnlineStatus: View

        init {
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo)
            userOnlineStatus = itemView.findViewById(R.id.userOnlineStatus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        val userInfo: String = "${user.name} ${user.lastName} ${user.age}"
        val bgColorId = if (user.online) R.drawable.circle_green else R.drawable.circle_red

        holder.userOnlineStatus.background =
            ContextCompat.getDrawable(holder.itemView.context, bgColorId)

        holder.textViewUserInfo.text = userInfo

        holder.itemView.setOnClickListener {
            if (::onUserClickListener.isInitialized) {
                onUserClickListener.onUserClick(user)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}