package com.johnturkson.githubapp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO Rename/refactor to GitHubHistory (storing a list of GitHubCommitHistory objects) due to API
//   differences between GET /repos/:owner/:repo/commits and
//   GET /repos/:owner/:repo/git/commits/:commit_sha endpoints
@Serializable
data class GitHubCommit(
    // TODO tied to GET /repos/:owner/:repo/commits endpoint
    // TODO convert to URL
    val url: String,
    val sha: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("commit") val information: CommitInformation,
    // TODO handle null if author/committer does not have a GitHub account
    val author: GitHubUser,
    // TODO handle null if author/committer does not have a GitHub account
    val committer: GitHubUser
)

@Serializable
data class CommitInformation(
    val url: String,
    val author: CommitAuthorInformation,
    val committer: CommitAuthorInformation,
    val message: String)

@Serializable
data class CommitAuthorInformation(val date: String, val name: String, val email: String)
