package ru.zhiev.githubviewer.presentation.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.zhiev.githubviewer.Constants
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.ActivityAuthBinding
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import ru.zhiev.githubviewer.presentation.main.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var appRepository: RepositoryModel
    private lateinit var viewModel: AuthViewModel
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)

        if (!tokenManager.accessToken.isNullOrEmpty()) {
            startMainActivity(tokenManager.accessToken!!)
        } else {
            webView = binding.webView
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            setupWebView()
        }

        binding.loginButton.setOnClickListener {
            binding.loginButton.visibility = View.GONE
            binding.progressBar.isVisible = true
            loadUrlInWebView()
        }

        appRepository = (applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(workWithGitHubUseCase)
        )[AuthViewModel::class.java]

        viewModel.accessToken.observe(this) { token ->
            if (token.accessToken.isNotEmpty()) {
                startMainActivity(token.accessToken)
                tokenManager.accessToken = token.accessToken
            } else {
                Toast.makeText(this, "Error: Empty access token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity(accessToken: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.ACCESS_TOKEN, accessToken)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
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
    }

    private fun loadUrlInWebView() {
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

    private fun handleUrl(url: String) {
        val uri = Uri.parse(url)
        val githubCode = uri.getQueryParameter("code") ?: ""
        if (githubCode != "") {
            binding.progressBar.isVisible = true
            viewModel.getAccessToken(githubCode)
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error: no code", Toast.LENGTH_SHORT).show()
        }
    }
}