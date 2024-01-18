package ru.zhiev.githubviewer.domain.models

import com.google.gson.annotations.SerializedName

data class TokenModel (
    @SerializedName("access_token")
    val accessToken: String,
)