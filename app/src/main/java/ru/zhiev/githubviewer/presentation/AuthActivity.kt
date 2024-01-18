package ru.zhiev.githubviewer.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.Constants
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.data.TokenManager
import ru.zhiev.githubviewer.databinding.ActivityAuthBinding
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var appRepository: RepositoryModel
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)

        appRepository = (applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(workWithGitHubUseCase)
        )[AuthViewModel::class.java]

        val webView: WebView = binding.webView

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        webView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        binding.loginButton.setOnClickListener { view ->
            binding.loginButton.visibility = View.GONE
            binding.progressBar.isVisible = true
            webView.loadUrl(
                "${Constants.GITHUB_URL}login/oauth/authorize?client_id=${Constants.CLIENT_ID}&scope=repo"
            )
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webView.visibility = View.VISIBLE
                    binding.progressBar.isVisible = false
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val url = request?.url.toString()
                    if (url.contains("?code=")) {
                        if (url.contains("?code=")) {
                            handleUrl(url)
                            webView.visibility = View.GONE
                        }
                        return true
                    }
                    return false
                }
            }
        }

        viewModel.accessToken.observe(this) { token ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.ACCESS_TOKEN, token.accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            tokenManager.accessToken = token.accessToken
        }
    }

    private fun handleUrl(url: String) {
        val uri = Uri.parse(url)
        val githubCode = uri.getQueryParameter("code") ?: ""
        if (githubCode != "") {
            binding.progressBar.isVisible = true
            viewModel.getAccessToken(githubCode)
            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}