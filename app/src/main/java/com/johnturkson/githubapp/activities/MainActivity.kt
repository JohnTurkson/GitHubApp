package com.johnturkson.githubapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.adapters.GitHubRepositoryViewAdapter
import com.johnturkson.githubapp.adapters.GitHubRepositoryViewHolder
import com.johnturkson.githubapp.api.GitHubApi
import com.johnturkson.githubapp.api.GitHubRepository
import com.johnturkson.githubapp.api.GitHubUser
import com.johnturkson.githubapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@UnstableDefault
class MainActivity : AppCompatActivity(), CoroutineScope {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainDrawerLayout: DrawerLayout
    private lateinit var mainActivityLayout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var repositoryViewTitle: TextView
    private lateinit var repositoriesView: RecyclerView
    private lateinit var repositoriesViewManager: RecyclerView.LayoutManager
    private lateinit var repositoriesViewAdapter: RecyclerView.Adapter<GitHubRepositoryViewHolder>
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationDrawer: NavigationView
    
    private val repositories = mutableListOf<GitHubRepository>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initializeTimber()
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        mainDrawerLayout = binding.mainActivityDrawerLayout
        mainActivityLayout = binding.mainActivityLayout
        
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onToolbarNavigationClicked(it) }
        
        searchBar = binding.userSearchBar
        searchButton = binding.searchButton
        searchButton.setOnClickListener {
            searchForUsers(searchBar.text.toString())
            // searchForRepositories(searchBar.text.toString())
        }
        
        repositoryViewTitle = binding.repositoryResultsTitle
        repositoriesViewManager = LinearLayoutManager(this)
        repositoriesViewAdapter = GitHubRepositoryViewAdapter(repositories)
        repositoriesView = binding.githubRepositories.apply {
            layoutManager = repositoriesViewManager
            adapter = repositoriesViewAdapter
        }
        
        bottomNavigationView = binding.navViewBottom
        bottomNavigationView.setOnNavigationItemSelectedListener {
            onBottomNavigationViewItemSelected(it)
        }
        
        navigationDrawer = binding.navigationDrawer
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
        Toast.makeText(this, "navigation selected", Toast.LENGTH_SHORT).show()
        openNavigationDrawer()
    }
    
    private fun openNavigationDrawer() {
        mainDrawerLayout.openDrawer(navigationDrawer)
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
    
    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO configure timber when non-debug
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("started timber")
    }
    
    // TODO refactor into separate object/class
    private fun searchForUsers(name: String) {
        if (name.isEmpty()) return
        launch {
            Timber.d("request to find user with name $name")
            val response = withContext(Dispatchers.IO) {
                GitHubApi.client.searchUsers(name).await()
            }
            populateUserSearchResults(response.users)
        }
    }
    
    // TODO refactor into separate object/class
    private fun populateUserSearchResults(users: List<GitHubUser>) {
        val displayusersIntent = Intent(this, UserSearchResultsActivity::class.java)
        val json = GitHubApi.encoder.stringify(GitHubUser.serializer().list, users)
        Timber.v(json)
        Timber.d("add users json as extra")
        displayusersIntent.putExtra(UserSearchResultsActivity.EXTRA_USERS, json)
        Timber.d("start activity to display users")
        startActivity(displayusersIntent)
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
    
    // TODO decouple from MainActivity
    private fun updateRepositoriesView(repositories: List<GitHubRepository>) {
        val displayRepositoriesIntent = Intent(this, RepositorySearchResultsActivity::class.java)
        val json = GitHubApi.encoder.stringify(GitHubRepository.serializer().list, repositories)
        Timber.v(json)
        Timber.d("add json as extra")
        displayRepositoriesIntent.putExtra(RepositorySearchResultsActivity.EXTRA_REPOSITORIES, json)
        Timber.d("start activity to display results")
        startActivity(displayRepositoriesIntent)
    }
    
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
}
