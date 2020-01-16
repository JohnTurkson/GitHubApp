package com.johnturkson.githubapp.activities


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.adapters.GitHubRepositoryViewAdapter
import com.johnturkson.githubapp.adapters.GitHubRepositoryViewHolder
import com.johnturkson.githubapp.api.GitHubApi
import com.johnturkson.githubapp.api.GitHubRepository
import com.johnturkson.githubapp.databinding.ActivityRepositorySearchResultsBinding
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.list
import timber.log.Timber

@UnstableDefault
class RepositorySearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepositorySearchResultsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var resultsTitle: TextView
    private lateinit var resultsView: RecyclerView
    private lateinit var resultsViewManager: RecyclerView.LayoutManager
    private lateinit var resultsViewAdapter: RecyclerView.Adapter<GitHubRepositoryViewHolder>
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var results: List<GitHubRepository>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val json = intent.getStringExtra(EXTRA_REPOSITORIES)
        require(json != null)
        results = GitHubApi.encoder.parse(GitHubRepository.serializer().list, json)
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_search_results)
        toolbar = binding.searchResultsToolbar
        setSupportActionBar(toolbar)
        resultsTitle = binding.resultsTitle
        
        resultsView = binding.repositories
        resultsViewManager = LinearLayoutManager(this)
        resultsViewAdapter = GitHubRepositoryViewAdapter(results)
        resultsView = binding.repositories.apply {
            layoutManager = resultsViewManager
            adapter = resultsViewAdapter
        }
        
        bottomNavigationView = binding.navViewBottom
        bottomNavigationView.setOnNavigationItemSelectedListener {
            onBottomNavigationViewItemSelected(it)
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repository_search_results_menu, menu)
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO implement action when menu item is selected
        return when (item.itemId) {
            // TODO replace placeholder
            R.id.placeholder -> true // TODO replace
                .also { Timber.d("placeholder menu item selected") }
                .let { true }
            R.id.settings -> true // TODO replace
                .also { Timber.d("settings menu item selected") }
                .let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    // TODO refactor into object to reduce duplication (pass in context)
    private fun onBottomNavigationViewItemSelected(item: MenuItem): Boolean {
        // TODO implement bottom navigation
        Timber.d("bottom navigation view item selected")
        return when (item.itemId) {
            R.id.navigation_search -> Toast.makeText(
                this,
                "search",
                Toast.LENGTH_SHORT
            ).show()
                .also { Timber.d("search tab selected") }
                .let { true }
            R.id.navigation_users -> Toast.makeText(
                this,
                "users",
                Toast.LENGTH_SHORT
            ).show()
                .also { Timber.d("user tab selected") }
                .let { true }
            R.id.navigation_repositories -> Toast.makeText(
                this,
                "repositories",
                Toast.LENGTH_SHORT
            ).show()
                .also { Timber.d("repositories tab selected") }
                .let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    companion object {
        const val EXTRA_REPOSITORIES: String = "repositories"
    }
}
