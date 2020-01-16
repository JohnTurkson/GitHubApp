package com.johnturkson.githubapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.activities.RepositorySearchResultsActivity
import com.johnturkson.githubapp.api.GitHubApi
import com.johnturkson.githubapp.api.GitHubRepository
import com.johnturkson.githubapp.api.GitHubUser
import kotlinx.android.synthetic.main.user_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.list
import retrofit2.await
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
        
        // TODO handle from UserSearchResultsActivity
        holder.view.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // TODO refactor into separate function (same functionality in MainActivity)
                val repositories = GitHubApi.client.getRepositories(user.username).await()
                val displayRepositoriesIntent =
                    Intent(holder.view.context, RepositorySearchResultsActivity::class.java)
                val json =
                    GitHubApi.encoder.stringify(GitHubRepository.serializer().list, repositories)
                Timber.v(json)
                Timber.d("add json as extra")
                displayRepositoriesIntent.putExtra(
                    RepositorySearchResultsActivity.EXTRA_REPOSITORIES,
                    json
                )
                Timber.d("start activity to display results")
                startActivity(holder.view.context, displayRepositoriesIntent, null)
            }
        }
    }
    
    override fun onClick(v: View?) {
        // TODO implement
        Timber.d("user clicked")
    }
}
