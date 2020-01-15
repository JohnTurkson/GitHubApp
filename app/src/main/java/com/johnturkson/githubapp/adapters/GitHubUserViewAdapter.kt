package com.johnturkson.githubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.api.GitHubUser
import kotlinx.android.synthetic.main.user_list_item.view.*
import timber.log.Timber

class GitHubUserViewAdapter(private val users: List<GitHubUser>) :
    RecyclerView.Adapter<GitHubUserViewHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)
        return GitHubUserViewHolder(view)
    }
    
    override fun getItemCount(): Int {
        return users.size
    }
    
    override fun onBindViewHolder(holder: GitHubUserViewHolder, position: Int) {
        val user = users[position]
        Glide.with(holder.view).load(user.avatar).into(holder.view.user_avatar)
        // TODO user may not have a name registered
        // holder.name.text = user.name
        holder.username.text = user.username
        holder.view.setOnClickListener {
            // TODO display user repositories in new activity on selection
        }
    }
    
    override fun onClick(v: View?) {
        // TODO implement
        Timber.d("user clicked")
    }
}
