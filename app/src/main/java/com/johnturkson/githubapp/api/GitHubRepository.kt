package com.johnturkson.githubapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO serialize Strings as URLs
@Serializable
data class GitHubRepository(
    val id: Int,
    @SerialName("node_id") val nodeId: String,
    val owner: GitHubUser,
    val name: String,
    @SerialName("html_url") val url: String,
    val description: String?
)

// TODO serialize Strings as URLs
@Serializable
data class GitHubUser(
    val login: String,
    val id: Int,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatar: String,
    @SerialName("html_url") val url: String
)

// TODO serialize Strings to [Zoned?]DateTime objects
@Serializable
data class RepositoryTimeMetadata(
    @SerialName("created_at") val createdTime: String,
    @SerialName("updated_at") val lastUpdatedTime: String,
    @SerialName("pushed_at") val lastPushedTime: String
)