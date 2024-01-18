package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CURRENT_USER_ID = "current_id"
        private const val EXTRA_OTHER_USER_ID = "other_id"

        fun newIntent(ctx: Context, currentUserId: String, otherUserId: String): Intent {
            val intent = Intent(ctx, ChatActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
            intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId)
            return intent
        }
    }

    private lateinit var textViewChatTitle: TextView
    private lateinit var onlineStatus: View
    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var editTextSendMessage: EditText
    private lateinit var imageViewSendMessage: ImageView

    private lateinit var currentUserId: String
    private lateinit var otherUserId: String

    private lateinit var messagesAdapter: MessagesAdapter

    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFactory: ChatViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        initViews()
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID).toString()
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID).toString()
        viewModelFactory = ChatViewModelFactory(currentUserId, otherUserId)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]

        observeViewModel()




        messagesAdapter = MessagesAdapter(currentUserId)
        recyclerViewMessages.adapter = messagesAdapter


        imageViewSendMessage.setOnClickListener {
            val message = Message(
                editTextSendMessage.text.toString().trim(), currentUserId, otherUserId
            )
            viewModel.sendMessage(message)
        }

    }


    private fun observeViewModel() {
        viewModel.messages.observe(this) { messages ->
            messagesAdapter.setMessages(messages)
        }
        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.messageSent.observe(this) {
            if (it) {
                editTextSendMessage.setText("")
            }
        }

        viewModel.otherUser.observe(this) {user ->
            val userInfo = "${user.name} ${user.lastName}"
            textViewChatTitle.text = userInfo
            val bgColorId = if (user.online) R.drawable.circle_green else R.drawable.circle_red

            onlineStatus.background =
                ContextCompat.getDrawable(this, bgColorId)
        }

    }

    private fun initViews() {
        textViewChatTitle = findViewById(R.id.textViewChatTitle)
        onlineStatus = findViewById(R.id.onlineStatus)
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        editTextSendMessage = findViewById(R.id.editTextSendMessage)
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUserOnline(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserOnline(false)
    }
}