package com.johnturkson.githubapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.databinding.ActivityRepositorySearchResultsBinding
import timber.log.Timber

class RepositorySearchResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRepositorySearchResultsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var resultsTitle: TextView
    private lateinit var resultsView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_search_results)
        toolbar = binding.searchResultsToolbar
        setSupportActionBar(toolbar)
        resultsTitle = binding.resultsTitle
        resultsView = binding.results
        bottomNavigationView = binding.navViewBottom
        bottomNavigationView.setOnNavigationItemSelectedListener { onBottomNavigationViewItemSelected(it) }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repository_search_results_menu, menu)
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO implement action when menu item is selected
        return when (item.itemId) {
            R.id.placeholder -> true // TODO replace
                .also { Timber.d("placeholder menu item selected") }
                .let { true }
            R.id.settings -> true // TODO replace
                .also {Timber.d("settings menu item selected")}
                .let { true }
            else -> super.onOptionsItemSelected(item)
        }
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
}
