package com.johnturkson.githubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.adapters.GitHubRepositoryAdapter
import com.johnturkson.githubapp.api.GitHubApi
import com.johnturkson.githubapp.api.GitHubRepository
import com.johnturkson.githubapp.databinding.ActivityMainBinding
import kotlinx.serialization.UnstableDefault
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@UnstableDefault
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityLayout: ConstraintLayout
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var repositoryViewTitle: TextView
    private lateinit var repositoryView: TextView
    
    private lateinit var repositories: RecyclerView
    private lateinit var repositoriesViewAdapter: GitHubRepositoryAdapter
    private lateinit var repositoriesViewManager: RecyclerView.LayoutManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initializeTimber()
        
        repositoriesViewManager = LinearLayoutManager(this)
        repositoriesViewAdapter = GitHubRepositoryAdapter()
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        mainActivityLayout = binding.mainActivityLayout
        searchBar = binding.userSearchBar
        searchButton = binding.searchButton
        repositoryViewTitle = binding.repositoryResultsTitle
        repositoryView = binding.repositoryResults
        repositories = binding.githubRepositories.apply {
            layoutManager = repositoriesViewManager
            adapter = repositoriesViewAdapter
        }
        
        binding.githubRepositories.adapter = GitHubRepositoryAdapter()
        searchButton.setOnClickListener {
            searchForUser(searchBar.text.toString())
        }
    }
    
    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO configure timber when non-debug
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("started timber")
    }
    
    private fun searchForUser(username: String) {
        Timber.d("request made for User $username")
        GitHubApi.client
            .getRepositories(username)
            // TODO update failure callback
            // TODO replace wrapper callback class with object declarations
            .enqueue(getRepositoryResponseCallback)
    }
    
    private val getRepositoryResponseCallback = object : Callback<List<GitHubRepository>> {
        override fun onResponse(
            call: Call<List<GitHubRepository>>,
            response: Response<List<GitHubRepository>>
        ) {
            if (response.isSuccessful) {
                // TODO improve error message
                Timber.d("response success")
                response.body()?.forEach { Timber.v(it.toString()) }
                    ?: Timber.v("response body is null")
                // TODO log when serialized response is invalid
                val repositories = response.body()
                require(repositories != null)
                updateRepositoriesView(repositories)
            } else {
                Timber.d("response failed")
                // TODO handle failure
            }
        }
        
        override fun onFailure(call: Call<List<GitHubRepository>>, t: Throwable) {
            Timber.d("failure")
            Timber.e(t)
            // TODO handle failure
        }
    }
    
    private fun updateRepositoriesView(repositories: List<GitHubRepository>) {
        require(repositories.isNotEmpty())
        repositoryViewTitle.text = getString(R.string.repository_results_title_text)
        repositoryView.text = repositories.random().name
        // TODO not working
        repositoriesViewAdapter.repositories = repositories
        repositoriesViewAdapter.notifyDataSetChanged()
        Timber.d("updated repository text view")
    }
}
