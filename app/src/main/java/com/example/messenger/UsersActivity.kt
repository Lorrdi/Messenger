package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class UsersActivity : AppCompatActivity() {
    private lateinit var currentUserId: String
    private lateinit var recyclerViewUsers: RecyclerView
    private val viewModel: UsersViewModel by viewModels()

    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter()
    }

    companion object {
        private const val EXTRA_CURRENT_USER_ID = "current_id"

        fun newIntent(ctx: Context, currentUserId: String): Intent {
            val intent = Intent(ctx, UsersActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
            return intent
        }

    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { firebaseUser ->
            if (firebaseUser == null) {
                startActivity(LoginActivity.newIntent(this))
                finish()
            }
        }
        viewModel.users.observe(this) { users ->
            usersAdapter.setUsers(users)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        initViews()
        observeViewModel()

        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID).toString()

        usersAdapter.setOnUserClickListener(object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = ChatActivity.newIntent(this@UsersActivity, currentUserId, user.id)
                startActivity(intent)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onResume() {
        super.onResume()
        viewModel.setUserOnline(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserOnline(false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout) viewModel.logout()
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        recyclerViewUsers.adapter = usersAdapter
    }
}