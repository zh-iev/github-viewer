package ru.zhiev.githubviewer.data

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.zhiev.githubviewer.Constants
import ru.zhiev.githubviewer.data.apimodels.GitHubRepositoryApiModel
import ru.zhiev.githubviewer.data.apimodels.TokenApiModel
import ru.zhiev.githubviewer.data.apimodels.UserApiModel

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
}

