package com.johnturkson.githubapp.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_list_item.view.*

class GitHubUserViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    var avatar: ImageView = view.user_avatar
    var name: TextView = view.user_name
    var username: TextView = view.user_username
}
