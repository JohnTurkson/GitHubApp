package com.johnturkson.githubapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO serialize Strings as URLs
@Serializable
data class GitHubUser(
    @SerialName("login") val username: String,
    val id: Int,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatar: String,
    @SerialName("html_url") val url: String
)
