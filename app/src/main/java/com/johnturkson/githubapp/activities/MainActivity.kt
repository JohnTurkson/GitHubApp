package com.johnturkson.githubapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var toolbar: Toolbar
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var repositoryViewTitle: TextView
    private lateinit var repositoryView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    
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
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onToolbarNavigationClicked(it) }
        searchBar = binding.userSearchBar
        searchButton = binding.searchButton
        repositoryViewTitle = binding.repositoryResultsTitle
        repositoryView = binding.repositoryResults
        repositories = binding.githubRepositories.apply {
            layoutManager = repositoriesViewManager
            adapter = repositoriesViewAdapter
        }
        bottomNavigationView = binding.navViewBottom
        bottomNavigationView.setOnNavigationItemSelectedListener { onBottomNavigationViewItemSelected(it) }
        
        binding.githubRepositories.adapter = GitHubRepositoryAdapter()
        searchButton.setOnClickListener {
            searchForRepositories(searchBar.text.toString())
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Timber.d("onCreateOptionsMenu invoked")
        menuInflater.inflate(R.menu.settings_menu, menu)
        Timber.d("options menu inflated")
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO implement toolbar menu
        Timber.d("toolbar options item selected")
        return when (item.itemId) {
            R.id.settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                .also { Timber.d("settings menu item selected") }
                .let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun onToolbarNavigationClicked(view: View) {
        Timber.d("toolbar navigation menu button clicked")
        // TODO open navigation drawer
        Toast.makeText(this, "navigation selected", Toast.LENGTH_SHORT).show()
    }
    
    // TODO refactor into object to reduce duplication (pass in context)
    private fun onBottomNavigationViewItemSelected(item: MenuItem): Boolean {
        // TODO implement bottom navigation
        Timber.d("bottom navigation view item selected")
        // TODO remove
        val testIntent = Intent(this, RepositorySearchResultsActivity::class.java)
        return when (item.itemId) {
            R.id.navigation_search -> Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
                .also { Timber.d("search tab selected") }
                .let { true }
            R.id.navigation_users -> startActivity(testIntent)
                .also { Timber.d("users tab selected") }
                .let { true }
            // R.id.navigation_users -> Toast.makeText(this, "users", Toast.LENGTH_SHORT).show()
            //     .also { Timber.d("user tab selected") }
            //     .let { true }
            R.id.navigation_repositories -> Toast.makeText(this, "repositories", Toast.LENGTH_SHORT).show()
                .also { Timber.d("repositories tab selected") }
                .let { true }
            else -> super.onOptionsItemSelected(item)
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
    
    private fun searchForRepositories(username: String) {
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
