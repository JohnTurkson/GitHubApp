package com.johnturkson.githubapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.api.GitHubRepository

class GitHubRepositoryViewAdapter(private val repositories: List<GitHubRepository>) :
    RecyclerView.Adapter<GitHubRepositoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_list_item, parent, false)
        return GitHubRepositoryViewHolder(textView)
    }
    
    override fun getItemCount(): Int {
        return repositories.size
    }
    
    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        holder.textView.text = repositories[position].name
    }
}