package ru.zhiev.githubviewer.data.apimodels

import com.google.gson.annotations.SerializedName

data class TokenApiModel (
    @SerializedName("access_token")
    val accessToken: String,
)