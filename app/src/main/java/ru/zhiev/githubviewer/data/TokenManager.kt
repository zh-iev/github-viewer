package ru.zhiev.githubviewer.data

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context, private val fileName: String = "GitHubViewer") {
    private var preference = context.getSharedPreferences(fileName, PREF_MODE)

    companion object{
        private const val PREF_MODE = Context.MODE_PRIVATE
        private val PREF_ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    var accessToken: String?
        get() = preference.getString(PREF_ACCESS_TOKEN, null)
        set(value) {
            preference.edit().putString(PREF_ACCESS_TOKEN, value).apply()
        }
}
