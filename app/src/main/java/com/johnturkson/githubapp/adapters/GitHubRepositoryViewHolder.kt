package com.johnturkson.githubapp.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.repository_list_item.view.*

class GitHubRepositoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.repository_name
    var description: TextView = view.repository_description
}
