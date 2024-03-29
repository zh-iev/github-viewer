package ru.zhiev.githubviewer.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.zhiev.githubviewer.GitHubViewerApplication
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.databinding.ActivityMainBinding
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import ru.zhiev.githubviewer.presentation.auth.AuthActivity
import ru.zhiev.githubviewer.TokenManager
import ru.zhiev.githubviewer.databinding.NavHeaderMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var appRepository: RepositoryModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
        setSupportActionBar(binding.appBarMain.toolbar)

        appRepository = (applicationContext as GitHubViewerApplication).repository
        val workWithGitHubUseCase = WorkWithGitHubUseCase(appRepository)
        viewModel = ViewModelProvider(this,
            MainViewModelFactory(workWithGitHubUseCase)
        )[MainViewModel::class.java]

        val accessToken = tokenManager.accessToken
        accessToken?.let {
            viewModel.getUserData(it)
        }

        val navHeaderView = binding.navView.getHeaderView(0)
        val headerBinding = NavHeaderMainBinding.bind(navHeaderView)
        viewModel.userData.observe(this) {
            if (!viewModel.wasWelcome) {
                Toast.makeText(this, "${getString(R.string.welcome)} ${it.name ?: ""}!", Toast.LENGTH_SHORT).show()
                viewModel.wasWelcome = true
            }
            Glide.with(this)
                .load(it.avatarUrl)
                .apply(RequestOptions().circleCrop())
                .into(headerBinding.userIcon)
            headerBinding.nickname.text = it.name
            headerBinding.login.text = it.login
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_repositories, R.id.nav_search, R.id.nav_issues
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                tokenManager.clearAccessToken()
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}