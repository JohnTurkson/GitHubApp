package com.johnturkson.githubapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.api.GitHubRepository

class GitHubRepositoryAdapter : RecyclerView.Adapter<GitHubRepositoryViewHolder>() {
    var repositories = listOf<GitHubRepository>()
    
    override fun getItemCount(): Int {
        return repositories.size
    }
    
    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        val repository = repositories[position]
        holder.textView.text = repository.name
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.github_repository_item, parent, false) as TextView
        return GitHubRepositoryViewHolder(view)
    }
}