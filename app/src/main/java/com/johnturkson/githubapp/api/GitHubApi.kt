package com.johnturkson.githubapp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.StringFormat
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

// TODO move into test class
@UnstableDefault
fun main() {
    val username = "JohnTurkson"
    val name = "John Turkson"
    val repository = "GitHubApp"
    
    val repositories = GitHubApi.client.getRepositories(username)
    // TODO extract callback components (onResponse, onFailure) to separate functions
    repositories.enqueue(object : Callback<List<GitHubRepository>> {
        override fun onResponse(
            call: Call<List<GitHubRepository>>,
            response: Response<List<GitHubRepository>>
        ) {
            if (response.isSuccessful) {
                response.body()?.forEach { println(it) } ?: println("No repositories found")
            } else {
                // TODO handle 4xx status codes
                println("Unable to obtain repositories for $username")
            }
        }
        
        override fun onFailure(call: Call<List<GitHubRepository>>, t: Throwable) {
            Timber.d("failed when getting repositories for $username")
            t.printStackTrace()
            // TODO handle failure
        }
    })
    
    val commits: Call<List<GitHubCommit>> = GitHubApi.client.getCommits(username, repository)
    // TODO extract callback components (onResponse, onFailure) to separate functions
    commits.enqueue(object : Callback<List<GitHubCommit>> {
        override fun onResponse(
            call: Call<List<GitHubCommit>>,
            response: Response<List<GitHubCommit>>
        ) {
            if (response.isSuccessful) {
                response.body()?.forEach { println(it) } ?: println("No repositories found")
            } else {
                // TODO handle 4xx status codes
                println("Unable to obtain repositories for $username")
            }
        }
        
        override fun onFailure(call: Call<List<GitHubCommit>>, t: Throwable) {
            Timber.d("failed when getting repositories for $username")
            t.printStackTrace()
            // TODO handle failure
        }
    })
    
    val searchUsers: Call<GitHubUserSearchResponse> = GitHubApi.client.searchUsers(name)
    searchUsers.enqueue(object : Callback<GitHubUserSearchResponse> {
        override fun onResponse(
            call: Call<GitHubUserSearchResponse>,
            response: Response<GitHubUserSearchResponse>
        ) {
            if (response.isSuccessful) {
                val users = response.body()?.users ?: emptyList()
                users.forEach { println(it) }
            } else {
                // TODO handle 4xx status codes
                println("Unable to find user with name $name")
            }
        }
        
        override fun onFailure(call: Call<GitHubUserSearchResponse>, t: Throwable) {
            Timber.d("failed when searching for user $name")
            t.printStackTrace()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })
}

@UnstableDefault
object GitHubApi {
    @UnstableDefault // Needed due to custom Json configuration
    val encoder: StringFormat =
        Json(JsonConfiguration(strictMode = false, encodeDefaults = false, prettyPrint = true))
    
    private const val endpoint: String = "https://api.github.com/"
    private val mediaType: MediaType = MediaType.get("application/json")
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(endpoint)
        .addConverterFactory(encoder.asConverterFactory(mediaType))
        .build()
    val client: GitHubApiService = retrofit.create(GitHubApiService::class.java)
}

interface GitHubApiService {
    @GET("users/{user}/repos")
    fun getRepositories(@Path("user") user: String): Call<List<GitHubRepository>>
    
    @GET("repos/{owner}/{repo}/commits")
    fun getCommits(
        @Path("owner") owner: String,
        @Path("repo") repository: String
    ): Call<List<GitHubCommit>>
    
    @GET("search/users")
    fun searchUsers(@Query("q") name: String): Call<GitHubUserSearchResponse>
}
