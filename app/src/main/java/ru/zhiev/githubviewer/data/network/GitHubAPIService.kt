package ru.zhiev.githubviewer.data.network

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zhiev.githubviewer.Constants
import ru.zhiev.githubviewer.data.apimodels.GitHubRepositoryApiModel
import ru.zhiev.githubviewer.data.apimodels.IssueApiModel
import ru.zhiev.githubviewer.data.apimodels.RepositorySearchApiModel
import ru.zhiev.githubviewer.data.apimodels.TokenApiModel
import ru.zhiev.githubviewer.data.apimodels.UserApiModel
import ru.zhiev.githubviewer.data.apimodels.UsersSearchApiModel

interface GitHubAPIService {

    @Headers("Accept: application/json")
    @POST(Constants.GITHUB_URL + "login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ) : TokenApiModel

    @Headers("Accept: application/json")
    @GET("user")
    suspend fun getUserData(
        @Header("authorization") token: String
    ): UserApiModel

    @Headers("Accept: application/json")
    @GET("user/repos")
    suspend fun getRepositories(
        @Header("authorization") token: String
    ): List<GitHubRepositoryApiModel>

    @Headers("Accept: application/json")
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): RepositorySearchApiModel

    @Headers("Accept: application/json")
    @GET("search/users")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): UsersSearchApiModel


    @Headers("Accept: */*")
    @GET("issues?filter=all&state=all")
    suspend fun getIssues(
        @Header("Authorization") token: String
    ): List<IssueApiModel>

    @Headers("Accept: application/json")
    @POST("user/repos")
    suspend fun createRepository(
        @Header("Authorization") token: String,
        @Body repositoryData: GitHubRepositoryApiModel
    )
}

