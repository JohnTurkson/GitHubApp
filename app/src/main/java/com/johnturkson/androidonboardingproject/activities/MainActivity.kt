package com.johnturkson.androidonboardingproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.androidonboardingproject.R
import com.johnturkson.androidonboardingproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityLayout: ConstraintLayout
    private lateinit var userRepositories: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityLayout = binding.mainActivityLayout
        userRepositories = binding.userRepositoriesView
    }
}
