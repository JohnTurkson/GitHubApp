package com.johnturkson.githubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.johnturkson.githubapp.R
import com.johnturkson.githubapp.api.GitHubApi
import com.johnturkson.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityLayout: ConstraintLayout
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var repositoryView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityLayout = binding.mainActivityLayout
        searchBar = binding.userSearchBar
        repositoryView = binding.repositoryPlaceholderText
        searchButton = binding.searchButton

        searchButton.setOnClickListener { searchForUser(searchBar.text.toString()) }
    }

    private fun searchForUser(username: String) {
        // TODO log request with timber
        println(username)
        repositoryView.text = GitHubApi.client.getRepositories(username).execute().body()!![0].name
        // binding.repository = GitHubApi.client.getRepositories(username).execute().body()!![0]
    }
}
