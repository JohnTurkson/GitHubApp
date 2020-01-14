package com.johnturkson.githubapp.adapters

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.johnturkson.githubapp.R
import kotlinx.android.synthetic.main.repository_list_item.view.*

class GitHubRepositoryViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    var textView: TextView = view.findViewById(R.id.item)
}