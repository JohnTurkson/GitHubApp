package com.johnturkson.githubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.api.GitHubRepository
import timber.log.Timber

class GitHubRepositoryViewAdapter(private val repositories: List<GitHubRepository>) :
    RecyclerView.Adapter<GitHubRepositoryViewHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_list_item, parent, false)
        return GitHubRepositoryViewHolder(view)
    }
    
    override fun getItemCount(): Int {
        return repositories.size
    }
    
    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        val repository = repositories[position]
        holder.name.text = repository.name
        holder.description.text = repository.description
            ?: holder.view.resources.getString(R.string.missing_description)
        holder.view.setOnClickListener {
            // TODO display repository commits
            
            // TODO remove
            Timber.d("clicked position $position - ${repository.name}")
                .let {
                    // TODO open in new window instead of Toast
                    Toast.makeText(
                        holder.name.context,
                        "clicked $position - ${holder.name.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    override fun onClick(view: View?) {
        // TODO implement
        Timber.d("repository clicked")
    }
}
