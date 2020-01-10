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

// Needed due to custom Json configuration
@UnstableDefault
val encoder: StringFormat =
    Json(JsonConfiguration(strictMode = false, encodeDefaults = false, prettyPrint = true))

@UnstableDefault
fun main() {
    val user: String = "JohnTurkson"
    val call: Call<List<GitHubRepository>> = GitHubApi.client.getRepositories(user)
    // TODO extract callback to separate function
    call.enqueue(object : Callback<List<GitHubRepository>> {
        override fun onResponse(
            call: Call<List<GitHubRepository>>,
            response: Response<List<GitHubRepository>>
        ) {
            if (response.isSuccessful) {
                response.body()?.forEach { println(it) } ?: println("No repositories found")
            } else {
                // TODO handle 4xx status codes
                println("Unable to obtain repositories for $user")
            }
        }

        override fun onFailure(call: Call<List<GitHubRepository>>, t: Throwable) {
            println("failed")
            t.printStackTrace()
            // TODO handle failure
        }
    })
}

@UnstableDefault
object GitHubApi {
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
}
