package com.johnturkson.githubapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserSearchResponse(
    @SerialName("total_count") val count: Int,
    @SerialName("incomplete_results") val incomplete: Boolean,
    @SerialName("items") val users: List<GitHubUser>
)
